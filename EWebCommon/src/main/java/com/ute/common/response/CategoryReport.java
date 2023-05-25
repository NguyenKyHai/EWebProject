package com.ute.common.response;

import java.math.BigDecimal;

public interface CategoryReport {

    Integer getCategoryId();
    String getCategoryName();
    Integer getOrderQuantity();
    Integer getProductQuantity();
    BigDecimal getGrossSale();
    BigDecimal getNetSale();
}
