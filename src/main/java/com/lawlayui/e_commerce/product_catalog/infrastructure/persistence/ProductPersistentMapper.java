package com.lawlayui.e_commerce.product_catalog.infrastructure.persistence;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.lawlayui.e_commerce.product_catalog.domain.model.Product;
import com.lawlayui.e_commerce.product_catalog.domain.value_object.ProductDescription;
import com.lawlayui.e_commerce.product_catalog.domain.value_object.ProductName;
import com.lawlayui.e_commerce.product_catalog.domain.value_object.ProductPhoto;
import com.lawlayui.e_commerce.product_catalog.domain.value_object.ProductPrice;
import com.lawlayui.e_commerce.product_catalog.domain.value_object.ProductSku;
import com.lawlayui.e_commerce.product_catalog.domain.value_object.ProductStatus;

@Mapper(componentModel = "spring")
public interface ProductPersistentMapper {

    @Mapping(source = "productName", target = "productName", qualifiedByName = "mapProductNameToString")
    @Mapping(source = "productDescription", target = "productDescription", qualifiedByName = "mapProductDescriptionToString")
    @Mapping(source = "productPhoto", target = "productPhoto", qualifiedByName = "mapProductPhotoToString")
    @Mapping(source = "productPrice", target = "productPrice", qualifiedByName = "mapProductPriceToBigDecimal")
    @Mapping(source = "productSku", target = "sku", qualifiedByName = "mapProductSkuToString")
    @Mapping(source = "status", target = "status", qualifiedByName = "mapProductStatusToString")
    ProductCatalogJpaEntity toEntity(Product product);

    default Product toDomain(ProductCatalogJpaEntity entity) {
        if (entity == null) {
            return null;
        }

        return new Product(
            entity.getProductName() != null ? new ProductName(entity.getProductName()) : null,
            entity.getProductDescription() != null ? new ProductDescription(entity.getProductDescription()) : null,
            entity.getProductPhoto() != null ? new ProductPhoto(entity.getProductPhoto()) : null,
            entity.getProductPrice() != null ? new ProductPrice(entity.getProductPrice()) : null,
            entity.getId() != null ? new ProductSku(entity.getId()) : null,
            entity.getStatus() != null ? ProductStatus.valueOf(entity.getStatus()) : null
        );
    }

    List<Product> toDomainList(List<ProductCatalogJpaEntity> entities);
    List<ProductCatalogJpaEntity> toEntityList(List<Product> products);

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

    @Named("mapProductSkuToString")
    default String mapProductSkuToString(ProductSku sku) {
        return sku != null ? sku.value() : null;
    }

    @Named("mapProductStatusToString")
    default String mapProductStatusToString(ProductStatus productStatus) {
        return productStatus != null ? productStatus.name() : null;
    }
}