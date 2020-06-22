package com.lyh.api.route;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
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
    LoadBalance loadBalance = LoadBalanceApi.loadBalance(providers);
    this.testLoadTest(loadBalance);
  }


  @Test
  @SneakyThrows
  public void testLoadBalance32() {
    List<Provider> providers = createProviderList(64);
    LoadBalance loadBalance = LoadBalanceApi.loadBalance(providers);
    this.testLoadTest(loadBalance);
  }

  @Test
  @SneakyThrows
  public void testLoadBalance60() {
    List<Provider> providers = createProviderList(60);
    LoadBalance loadBalance = LoadBalanceApi.loadBalance(providers);
    this.testLoadTest(loadBalance);
  }

  @Test
  @SneakyThrows
  public void testLoadBalance64() {
    List<Provider> providers = createProviderList(64);
    LoadBalance loadBalance = LoadBalanceApi.loadBalance(providers);
    this.testLoadTest(loadBalance);
  }

  @SneakyThrows
  private void testLoadTest(LoadBalance loadBalance) {
    CountDownLatch latch = new CountDownLatch(100);
    for (int t = 0; t < 100; t++) {
      new Thread(() -> {
        long start = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
          Provider provider = loadBalance.balance(i % LoadBalance.loopMax).use();
          if (provider == null) {
            throw new IllegalArgumentException("负载均衡计算失败");
          }
        }
        latch.countDown();
        System.out.println("耗时:" + (System.currentTimeMillis() - start));
      }).start();
    }
    latch.await();
    Assert.assertEquals(1000000, loadBalance.dump());
  }
}
