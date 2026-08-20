package com.lawlayui.e_commerce.product_catalog.application.port.in;

public record GetAllProductQuery(Long page, Long pageSize, String searchKey) {
    
}
