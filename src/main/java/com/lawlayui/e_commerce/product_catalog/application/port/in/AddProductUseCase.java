package com.lawlayui.e_commerce.product_catalog.application.port.in;

public interface AddProductUseCase {
    public ProductDto execute(AddProductCommand command);
}
