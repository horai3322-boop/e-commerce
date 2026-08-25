package com.lawlayui.e_commerce.product_catalog.application.port.in;

import java.util.List;

public interface SearchProductUseCase {
    public List<ProductDto> execute(SearchProductQuery query);    
}
