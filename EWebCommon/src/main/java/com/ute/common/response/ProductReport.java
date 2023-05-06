package com.ute.common.response;

import java.math.BigDecimal;

public interface ProductReport {
    String getProductName();
    BigDecimal getTotalPrice();
    BigDecimal getTotalCost();
    BigDecimal getProfit();
    Integer getQuantity();
    String getProductImage();
    String getCategoryName();
}
