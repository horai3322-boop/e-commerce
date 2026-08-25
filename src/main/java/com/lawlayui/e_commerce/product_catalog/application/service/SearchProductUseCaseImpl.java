package com.lawlayui.e_commerce.product_catalog.application.service;

import java.util.List;

import com.lawlayui.e_commerce.product_catalog.application.mapper.ProductMapping;
import com.lawlayui.e_commerce.product_catalog.application.port.in.ProductDto;
import com.lawlayui.e_commerce.product_catalog.application.port.in.SearchProductQuery;
import com.lawlayui.e_commerce.product_catalog.application.port.in.SearchProductUseCase;
import com.lawlayui.e_commerce.product_catalog.application.port.out.ProductCatalogRepository;

public class SearchProductUseCaseImpl implements SearchProductUseCase {
    private final ProductCatalogRepository productRepository;
    private final ProductMapping productMapping;

    public SearchProductUseCaseImpl(ProductCatalogRepository productRepository, ProductMapping productMapping) {
        this.productRepository = productRepository;
        this.productMapping = productMapping;
    }

    @Override
    public List<ProductDto> execute(SearchProductQuery query) {
        return productMapping.toDtoList(productRepository.getAll(query.page(), query.pageSize(), query.searchKey()));
    }
    
}
