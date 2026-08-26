package com.lawlayui.e_commerce.product_catalog.application.service;

import org.springframework.stereotype.Service;

import com.lawlayui.e_commerce.product_catalog.application.exception.ProductNotFoundException;
import com.lawlayui.e_commerce.product_catalog.application.mapper.ProductMapping;
import com.lawlayui.e_commerce.product_catalog.application.port.in.GetProductByIdQuery;
import com.lawlayui.e_commerce.product_catalog.application.port.in.GetProductByIdUseCase;
import com.lawlayui.e_commerce.product_catalog.application.port.in.ProductDto;
import com.lawlayui.e_commerce.product_catalog.application.port.out.ProductCatalogRepository;
import com.lawlayui.e_commerce.product_catalog.domain.value_object.ProductId;

@Service
public class GetProductByIdImpl implements GetProductByIdUseCase {
    private final ProductCatalogRepository productRepository;
    private final ProductMapping productMapping;

    public GetProductByIdImpl(ProductCatalogRepository productRepository, ProductMapping productMapping) {
        this.productRepository = productRepository;
        this.productMapping = productMapping;
    }

    @Override
    public ProductDto execute(GetProductByIdQuery query) {
        return productMapping.toDto(
            productRepository.getById(new ProductId(query.id()))
                .orElseThrow(() -> new ProductNotFoundException(query.id()))
        );
    }
}
