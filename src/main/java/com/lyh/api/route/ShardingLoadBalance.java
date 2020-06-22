package com.lyh.api.route;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 基于数据库分库路由sharding算法实现负载均衡
 */
public class ShardingLoadBalance implements LoadBalance {

  private final Map<Integer, List<Provider>> loop = new ConcurrentHashMap<>(32);
  boolean isAvg = false;

  public static ShardingLoadBalance of(List<Provider> providers) {
    ShardingLoadBalance nodeLoop = new ShardingLoadBalance();
    int providerPos = 0;
    if (providers.size() > loopMax) {
      int loop = providers.size() / loopMax;
      nodeLoop.initOkLoop(providers, loop * loopMax);
      providerPos = loop * loopMax;
    }
    nodeLoop.isAvg = providers.size() % loopMax == 0;
    if (!nodeLoop.isAvg) {
      final int firstIndex = providerPos;
      for (int i = 0; i < loopMax; i++) {
        if (providerPos >= providers.size()) {
          providerPos = firstIndex;
        }
        if (!nodeLoop.loop.containsKey(i)) {
          nodeLoop.loop.putIfAbsent(i, Collections.synchronizedList(new LinkedList<>()));
        }
        nodeLoop.loop.get(i).add(providers.get(providerPos));
        providerPos++;
      }
    }
    return nodeLoop;
  }

  public Provider balance(int dbIndex) {
    List<Provider> list = loop.get(dbIndex);
    if (this.isAvg) {
      return list.get(ThreadLocalRandom.current().nextInt(list.size()));
    } else {
      if (list.size() == 1) {
        return list.get(0);
      }
      if (new Random().nextInt(50) == 0) {
        Collections.sort(list);
      }
      return list.get(0);

      // int randomNum = new Random().nextInt(maxSize);
      // if (randomNum < (list.size() - 1) * lookMax) {
      // return list.get(new Random().nextInt(list.size() - 1));
      // } else {
      // return list.get(list.size() - 1);
      // }
    }
  }

  private void initOkLoop(List<Provider> providers, int maxSize) {
    for (int i = 0; i < maxSize; i++) {
      int key = i % loopMax;
      if (!loop.containsKey(key)) {
        loop.putIfAbsent(key, Collections.synchronizedList(new LinkedList<>()));
      }
      loop.get(key).add(providers.get(i));
    }
  }

  @Override
  public long dump() {
    final AtomicLong count = new AtomicLong();
    Set<String> keySet = new HashSet<>();
    for (Map.Entry<Integer, List<Provider>> entry : loop.entrySet()) {
      List<Provider> providers = entry.getValue();
      providers.forEach((p) -> {
        if (keySet.add(p.getHost())) {
          count.addAndGet(p.getCounter().longValue());
        }
      });
      StringBuilder builder = new StringBuilder();
      providers.forEach((p) -> {
        builder.append(p.toString()).append("|");
      });
      System.out.println(builder.toString());
    }
    return count.longValue();
  }

}
