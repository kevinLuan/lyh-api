package com.lyh.api.route;

import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;
import lombok.SneakyThrows;
import org.junit.Assert;
import org.junit.Test;

public class TestLoadBalance {

  private List<Provider> createProviderList(int size) {
    List<Provider> providers = new LinkedList<>();
    for (int i = 0; i < size; i++) {
      providers.add(Provider.of("192.168.1." + i, 8080, i));
    }
    return providers;
  }

  @Test
  @SneakyThrows
  public void testLoadBalance30() {
    List<Provider> providers = createProviderList(30);
    this.testLoadTest(ShardingLoadBalance.of(providers), System.out);
  }


  @Test
  @SneakyThrows
  public void testLoadBalance32() {
    List<Provider> providers = createProviderList(32);
    this.testLoadTest(ShardingLoadBalance.of(providers), System.out);
  }

  @Test
  @SneakyThrows
  public void testLoadBalance285() {
    List<Provider> providers = createProviderList(285);
    this.testLoadTest(ShardingLoadBalance.of(providers), System.out);
  }

  @Test
  @SneakyThrows
  public void testLoadBalance() {
    for (int i = 1; i < 1000; i++) {
      List<Provider> providers = createProviderList(i);
      this.testLoadTest(ShardingLoadBalance.of(providers), null);
    }
  }

  @Test
  @SneakyThrows
  public void testLoadBalance64() {
    List<Provider> providers = createProviderList(64);
    this.testLoadTest(ShardingLoadBalance.of(providers), System.out);
  }

  @SneakyThrows
  private void testLoadTest(LoadBalance loadBalance, PrintStream out) {
    CountDownLatch latch = new CountDownLatch(10);
    for (int t = 0; t < 10; t++) {
      new Thread(() -> {
        for (int i = 0; i < 100000; i++) {
          int dbIndex = ThreadLocalRandom.current().nextInt(LoadBalance.loopMax);
          Provider provider = loadBalance.balance(dbIndex).use();
          if (provider == null) {
            throw new IllegalArgumentException("负载均衡计算失败");
          }
        }
        latch.countDown();
      }).start();
    }
    latch.await();
    Assert.assertEquals(1000000, loadBalance.dump(out));
    LowHighPair pair = loadBalance.statLoad();
    if (pair.calcRatio() < 0.85f) {
      System.out.println("------------------------------------------");
      System.out.println(
          loadBalance.getProviderSize() + "  load ratio==>>" + pair.calcRatio() + " " + pair.getLow() + "~" + pair
              .getHigh());
    }
    Assert.assertTrue(pair.calcRatio() >= 0.75f);
  }
}
