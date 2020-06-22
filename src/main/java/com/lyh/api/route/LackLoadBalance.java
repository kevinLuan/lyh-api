package com.lyh.api.route;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 服务提供者比较少的情况下的负载均衡
 */
public class LackLoadBalance implements LoadBalance {

  // 服务提供者小于32
  private List<Provider> providers;
  private int lookMax = 32;

  public static LackLoadBalance of(List<Provider> providers) {
    LackLoadBalance loadBalance = new LackLoadBalance();
    loadBalance.providers = Collections.synchronizedList(providers);
    return loadBalance;
  }

  public Provider balance(int dbIndex) {
    if (providers.size() == lookMax) {
      //当服务提供者实例数等于sharding库的数量情况下，采用随机负载
      int index = ThreadLocalRandom.current().nextInt(providers.size());
      return providers.get(index);
    } else {
      return this.getMinReq();
    }
  }

  //服务提供者小于sharding数量时采用最小请求数负责
  private Provider getMinReq() {
    if (new Random().nextInt(50) == 0) {
      Collections.sort(providers);
    }
    return providers.get(0);
  }

  @Override
  public long dump() {
    AtomicLong count = new AtomicLong();
    for (int i = 0; i < providers.size(); i++) {
      Provider provider = providers.get(i);
      count.addAndGet(provider.getCounter().longValue());
      provider.toString();
      System.out.println(i + "->" + provider);
    }
    return count.get();
  }
}
