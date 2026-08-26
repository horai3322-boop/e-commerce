package com.lawlayui.e_commerce.product_catalog.application.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.lawlayui.e_commerce.product_catalog.application.port.in.ProductDto;
import com.lawlayui.e_commerce.product_catalog.domain.model.Product;

@Mapper(componentModel = "spring")
public interface ProductMapping {
    ProductDto toDto(Product product);
    Product toEntity(ProductDto productDto);
    List<ProductDto> toDtoList(List<Product> products);
    List<Product> toEntityList(List<ProductDto> productDtos);
}
