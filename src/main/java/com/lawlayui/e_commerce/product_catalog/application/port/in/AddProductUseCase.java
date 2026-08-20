package com.lawlayui.e_commerce.product_catalog.application.port.in;

import com.lawlayui.e_commerce.product_catalog.application.port.out.ProductRepository;

public class AddProductUseCase {
    private ProductRepository productRepository;

    public AddProductUseCase(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // public ProductDto execute(AddProductCommand command) {
        
    // }
}
