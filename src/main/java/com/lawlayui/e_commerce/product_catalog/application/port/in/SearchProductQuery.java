package com.lawlayui.e_commerce.product_catalog.application.port.in;

public record SearchProductQuery(Integer page, Integer pageSize, String searchKey) {
}
