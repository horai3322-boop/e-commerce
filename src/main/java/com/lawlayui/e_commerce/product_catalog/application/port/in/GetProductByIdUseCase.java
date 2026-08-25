package com.lawlayui.e_commerce.product_catalog.application.port.in;

public interface GetProductByIdUseCase {
    public ProductDto execute(GetProductByIdQuery query);
}
