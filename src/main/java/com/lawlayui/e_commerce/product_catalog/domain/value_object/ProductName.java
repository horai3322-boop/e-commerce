package com.lawlayui.e_commerce.product_catalog.domain.value_object;

import com.lawlayui.e_commerce.product_catalog.domain.exception.ProductValueBlankOrEmtpy;

public record ProductName(String productName) {
    public ProductName {
        if (productName.isBlank() || productName.isEmpty()) {
            throw new ProductValueBlankOrEmtpy("product name");
        }
    }
}
