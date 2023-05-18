package com.ute.common.response;

import java.math.BigDecimal;

public interface ProductItem {

    Integer getId();
    String getProductName();
    Long getTotalSold();
    Long getQuantity();
    String getProductImage();
    BigDecimal getProductPrice();
    float getDiscountPercent();
    String getCategoryName();

}
