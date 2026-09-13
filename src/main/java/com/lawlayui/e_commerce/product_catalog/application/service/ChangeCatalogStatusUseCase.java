package com.lawlayui.e_commerce.product_catalog.application.service;

import org.springframework.stereotype.Service;

import com.lawlayui.e_commerce.product_catalog.application.exception.CatalogNotFoundException;
import com.lawlayui.e_commerce.product_catalog.application.port.in.ChangeCatalogStatusCommand;
import com.lawlayui.e_commerce.product_catalog.application.port.out.ProductCatalogRepository;
import com.lawlayui.e_commerce.product_catalog.domain.model.Product;

@Service
public class ChangeCatalogStatusUseCase{
    private final ProductCatalogRepository productCatalogRepository;
    
    public ChangeCatalogStatusUseCase(ProductCatalogRepository productCatalogRepository) {
        this.productCatalogRepository = productCatalogRepository;
    }

    public void execute(ChangeCatalogStatusCommand command) {
        Product product = productCatalogRepository.getBySku(command.sku())
            .orElseThrow(() -> new CatalogNotFoundException(command.sku()));

        product.changeStatus(command.stock());
        productCatalogRepository.save(product);
    }
}
