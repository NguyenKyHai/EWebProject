package com.ute.common.response;

import java.math.BigDecimal;

public interface ProductItem {
    String getProductName();
    Long getTotalSold();
    Long getQuantity();
    String getProductImage();
    String getCategoryName();

}
