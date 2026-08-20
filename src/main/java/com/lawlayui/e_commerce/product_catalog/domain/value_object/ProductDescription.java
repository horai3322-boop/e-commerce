package com.lawlayui.e_commerce.product_catalog.domain.value_object;

import com.lawlayui.e_commerce.product_catalog.domain.exception.ProductValueBlankOrEmtpy;

public record ProductDescription(String desc) {
    public ProductDescription {
        if (desc.isBlank() || desc.isEmpty()) {
            throw new  ProductValueBlankOrEmtpy("product description");
        }
    }
}
