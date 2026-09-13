package com.lawlayui.e_commerce.product_catalog.application.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.lawlayui.e_commerce.product_catalog.application.mapper.ProductMapping;
import com.lawlayui.e_commerce.product_catalog.application.port.in.ProductDto;
import com.lawlayui.e_commerce.product_catalog.application.port.in.SearchProductQuery;
import com.lawlayui.e_commerce.product_catalog.application.port.out.ProductCatalogRepository;

@Service
public class SearchProductUseCase{
    private final ProductCatalogRepository productRepository;
    private final ProductMapping productMapping;

    public SearchProductUseCase(ProductCatalogRepository productRepository, ProductMapping productMapping) {
        this.productRepository = productRepository;
        this.productMapping = productMapping;
    }

    public List<ProductDto> execute(SearchProductQuery query) {
        return productMapping.toDtoList(productRepository.getAll(query.page(), query.pageSize(), "%" + query.searchKey().trim() + "%"));
    }
    
}
