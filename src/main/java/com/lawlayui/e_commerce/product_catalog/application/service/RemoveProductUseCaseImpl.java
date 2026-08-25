package com.lawlayui.e_commerce.product_catalog.application.service;


import com.lawlayui.e_commerce.product_catalog.application.port.in.RemoveProductCommand;
import com.lawlayui.e_commerce.product_catalog.application.port.in.RemoveProductUseCase;
import com.lawlayui.e_commerce.product_catalog.application.port.out.ProductCatalogRepository;

public class RemoveProductUseCaseImpl implements RemoveProductUseCase {
    private final ProductCatalogRepository productCatalogRepository;

    public RemoveProductUseCaseImpl(ProductCatalogRepository productCatalogRepository) {
        this.productCatalogRepository = productCatalogRepository;
    }

    @Override
    public void execute(RemoveProductCommand command) {
    }
    
}
