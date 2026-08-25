package com.lawlayui.e_commerce.product_catalog.application.port.in;

public record SearchProductQuery(int page, int pageSize, String searchKey) {
    
}
