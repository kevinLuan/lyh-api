package com.lyh.api.route;

import java.io.PrintStream;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
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
  private RandomLoadBalance lackLoadBalance = null;
  private List<Provider> providers;

  public static ShardingLoadBalance of(List<Provider> providers) {
    ShardingLoadBalance nodeLoop = new ShardingLoadBalance();
    nodeLoop.providers = providers;
    int providerPos = 0;
    if (providers.size() >= loopMax) {
      int loop = providers.size() / loopMax;
      nodeLoop.initOkLoop(providers, loop * loopMax);
      providerPos = loop * loopMax;
    }
    nodeLoop.isAvg = providers.size() % loopMax == 0;
    if (!nodeLoop.isAvg) {
      List<Provider> lastProviderList = providers.subList(providerPos, providers.size());
      nodeLoop.lackLoadBalance = RandomLoadBalance.of(lastProviderList);
    }
    return nodeLoop;
  }

  public Provider balance(int dbIndex) {
    List<Provider> list = loop.get(dbIndex);
    if (this.lackLoadBalance == null) {
      return list.get(ThreadLocalRandom.current().nextInt(list.size()));
    } else {
      int totalProvider = providers.size();
      if (ThreadLocalRandom.current().nextInt(totalProvider) < totalProvider - (totalProvider % loopMax)) {
        return list.get(ThreadLocalRandom.current().nextInt(list.size()));
      } else {
        return lackLoadBalance.balance(dbIndex);
      }
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
  public long dump(PrintStream out) {
    final AtomicLong count = new AtomicLong();
    Set<String> keySet = new HashSet<>();
    for (Map.Entry<Integer, List<Provider>> entry : loop.entrySet()) {
      List<Provider> providers = entry.getValue();
      StringBuilder builder = new StringBuilder();
      providers.forEach((p) -> {
        if (keySet.add(p.getHost())) {
          count.addAndGet(p.getCounter().longValue());
        }
        if (out != null) {
          builder.append(p.toString()).append("|");
        }
      });
      if (out != null) {
        out.println(builder.toString());
      }
    }
    if (lackLoadBalance != null) {
      return lackLoadBalance.dump(out) + count.longValue();
    }
    return count.longValue();
  }

  @Override
  public LowHighPair statLoad() {
    LowHighPair pair = new LowHighPair();
    providers.forEach((p) -> {
      if (pair.getLow() == -1) {
        pair.setLow(p.getCounter().get());
      }
      if (pair.getHigh() == -1) {
        pair.setHigh(p.getCounter().get());
      }
      pair.setHigh(Math.max(pair.getHigh(), p.getCounter().get()));
      pair.setLow(Math.min(pair.getLow(), p.getCounter().get()));
    });
    return pair;
  }

  @Override
  public int getProviderSize() {
    return providers.size();
  }

}
