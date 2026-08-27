package com.lawlayui.e_commerce.product_catalog.infrastructure.persistence;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.lawlayui.e_commerce.product_catalog.domain.model.Product;
import com.lawlayui.e_commerce.product_catalog.domain.value_object.ProductDescription;
import com.lawlayui.e_commerce.product_catalog.domain.value_object.ProductId;
import com.lawlayui.e_commerce.product_catalog.domain.value_object.ProductName;
import com.lawlayui.e_commerce.product_catalog.domain.value_object.ProductPhoto;
import com.lawlayui.e_commerce.product_catalog.domain.value_object.ProductPrice;

@Mapper(componentModel = "spring")
public interface ProductPersistentMapper {
    @Mapping(source = "productId", target = "productId", qualifiedByName = "mapProductIdToString")
    @Mapping(source = "productName", target = "productName", qualifiedByName = "mapProductNameToString")
    @Mapping(source = "productDescription", target = "productDescription", qualifiedByName = "mapProductDescriptionToString")
    @Mapping(source = "productPhoto", target = "productPhoto", qualifiedByName = "mapProductPhotoToString")
    @Mapping(source = "productPrice", target = "productPrice", qualifiedByName = "mapProductPriceToBigDecimal")
    ProductCatalogJpaEntity toEntity(Product product);
    Product toDomain(ProductCatalogJpaEntity entity);
    List<Product> toDomainList(List<ProductCatalogJpaEntity> entities);
    List<ProductCatalogJpaEntity> toEntityList(List<Product> products);

    @Named("mapProductIdToString")
    default String mapProductIdToString(ProductId productId) {
        return productId != null ? productId.id().toString() : null;
    }

    @Named("mapProductDescriptionToString")
    default String mapProductDescriptionToString(ProductDescription description) {
        return description != null ? description.desc() : null;
    }

    @Named("mapProductNameToString")
    default String mapProductNameToString(ProductName productName) {
        return productName != null ? productName.productName() : null;
    }

    @Named("mapProductPhotoToString")
    default String mapProductPhotoToString(ProductPhoto productPhoto) {
        return productPhoto != null ? productPhoto.filePath() : null;
    }

    @Named("mapProductPriceToBigDecimal")
    default java.math.BigDecimal mapProductPriceToBigDecimal(ProductPrice price) {
        return price != null ? price.price() : null;
    }
}
