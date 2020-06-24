package com.lyh.api.route;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 服务提供者比较少的情况下的负载均衡
 */
public class RandomLoadBalance implements LoadBalance {

  // 服务提供者小于32
  private List<Provider> providers;

  public static RandomLoadBalance of(List<Provider> providers) {
    RandomLoadBalance loadBalance = new RandomLoadBalance();
    loadBalance.providers = Collections.synchronizedList(providers);
    return loadBalance;
  }

  public Provider balance(int dbIndex) {
    int index = ThreadLocalRandom.current().nextInt(providers.size());
    return providers.get(index);
  }

  @Override
  public long dump(PrintStream out) {
    AtomicLong count = new AtomicLong();
    for (int i = 0; i < providers.size(); i++) {
      Provider provider = providers.get(i);
      count.addAndGet(provider.getCounter().longValue());
      if (out != null) {
        out.println(i + "->" + provider);
      }
    }
    return count.get();
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
