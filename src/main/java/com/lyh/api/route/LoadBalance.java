package com.lyh.api.route;

public interface LoadBalance {

  //sharding 库的数量
  int loopMax = 32;

  /**
   * sharing 负载
   *
   * @param dbIndex 分库索引号
   * @return
   */
  Provider balance(int dbIndex);

  long dump();
}
