package com.lawlayui.e_commerce.product_catalog.application.port.in;

import java.math.BigDecimal;

public record ProductDto(String productName, String productDescription, BigDecimal productPrice, String productPhoto, String sku, String status) {
    
}
