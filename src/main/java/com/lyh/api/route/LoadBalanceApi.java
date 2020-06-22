package com.lyh.api.route;

import java.util.List;

public class LoadBalanceApi {

  public static LoadBalance loadBalance(List<Provider> providers) {
    if (providers.size() > LoadBalance.loopMax) {
      return ShardingLoadBalance.of(providers);
    } else {
      return LackLoadBalance.of(providers);
    }
  }
}
