package com.lyh.api.route;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class LowHighPair {

  private long low = -1;
  private long high = -1;

  public float calcRatio() {
    BigDecimal decimal = new BigDecimal(low);
    BigDecimal result = decimal.divide(new BigDecimal(high), 2, BigDecimal.ROUND_HALF_UP);
    return result.floatValue();
  }
}
