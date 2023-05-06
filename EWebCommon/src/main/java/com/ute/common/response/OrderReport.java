package com.ute.common.response;

import java.math.BigDecimal;

public interface OrderReport {
    Integer getOrderQuantity();
    Integer getOrderDetailQuantity();
    BigDecimal getGrossSale();
    BigDecimal getNetSale();
}
