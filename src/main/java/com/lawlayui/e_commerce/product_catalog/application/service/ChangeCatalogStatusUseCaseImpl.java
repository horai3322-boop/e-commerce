package com.lawlayui.e_commerce.product_catalog.application.service;

import org.springframework.stereotype.Service;

import com.lawlayui.e_commerce.product_catalog.application.exception.ProductNotFoundException;
import com.lawlayui.e_commerce.product_catalog.application.port.in.ChangeCatalogStatusCommand;
import com.lawlayui.e_commerce.product_catalog.application.port.in.ChangeCatalogStatusUseCase;
import com.lawlayui.e_commerce.product_catalog.application.port.out.ProductCatalogRepository;
import com.lawlayui.e_commerce.product_catalog.domain.model.Product;

@Service
public class ChangeCatalogStatusUseCaseImpl implements ChangeCatalogStatusUseCase{
    private final ProductCatalogRepository productCatalogRepository;
    
    public ChangeCatalogStatusUseCaseImpl(ProductCatalogRepository productCatalogRepository) {
        this.productCatalogRepository = productCatalogRepository;
    }

    @Override
    public void execute(ChangeCatalogStatusCommand command) {
        Product product = productCatalogRepository.getBySku(command.sku())
            .orElseThrow(() -> new ProductNotFoundException(command.sku()));

        product.changeStatus(command.stock());
        productCatalogRepository.save(product);
    }
}
