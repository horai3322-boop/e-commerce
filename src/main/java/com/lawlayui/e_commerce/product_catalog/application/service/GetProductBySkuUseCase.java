package com.lawlayui.e_commerce.product_catalog.application.service;

import org.springframework.stereotype.Service;

import com.lawlayui.e_commerce.product_catalog.application.exception.CatalogNotFoundException;
import com.lawlayui.e_commerce.product_catalog.application.mapper.ProductMapping;
import com.lawlayui.e_commerce.product_catalog.application.port.in.GetProductBySkuQuery;
import com.lawlayui.e_commerce.product_catalog.application.port.in.ProductDto;
import com.lawlayui.e_commerce.product_catalog.application.port.out.ProductCatalogRepository;
import com.lawlayui.e_commerce.product_catalog.domain.model.Product;

@Service 
public class GetProductBySkuUseCase{
    private final ProductCatalogRepository productCatalogRepository;
    private final ProductMapping productMapping;
    
    public GetProductBySkuUseCase(ProductCatalogRepository productCatalogRepository, ProductMapping productMapping) {
        this.productCatalogRepository = productCatalogRepository;
        this.productMapping = productMapping;
    }

    public ProductDto execute(GetProductBySkuQuery query) {
        Product product = productCatalogRepository.getBySku(query.sku())
            .orElseThrow(() -> new CatalogNotFoundException(query.sku()));
        return productMapping.toDto(product);
    }
}
