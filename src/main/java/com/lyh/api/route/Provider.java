package com.lyh.api.route;

import java.util.concurrent.atomic.AtomicLong;
import lombok.Data;

@Data(staticConstructor = "of")
public class Provider implements Comparable<Provider> {

  private final String host;
  private final int port;
  private final int name;
  private AtomicLong counter = new AtomicLong();

  @Override
  public int compareTo(Provider o) {
    return Long.valueOf(counter.get()).compareTo(o.getCounter().get());
  }

  public Provider use() {
    this.counter.incrementAndGet();
    return this;
  }

  @Override
  public String toString() {
    return host + ":" + port + "/index:" + name + " (" + counter.get() + ")";
  }
}
