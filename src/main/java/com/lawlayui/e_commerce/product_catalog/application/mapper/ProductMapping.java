package com.lawlayui.e_commerce.product_catalog.application.mapper;

import java.math.BigDecimal;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.lawlayui.e_commerce.product_catalog.application.port.in.ProductDto;
import com.lawlayui.e_commerce.product_catalog.domain.model.Product;
import com.lawlayui.e_commerce.product_catalog.domain.value_object.ProductDescription;
import com.lawlayui.e_commerce.product_catalog.domain.value_object.ProductName;
import com.lawlayui.e_commerce.product_catalog.domain.value_object.ProductPhoto;
import com.lawlayui.e_commerce.product_catalog.domain.value_object.ProductPrice;
import com.lawlayui.e_commerce.product_catalog.domain.value_object.ProductSku;
import com.lawlayui.e_commerce.product_catalog.domain.value_object.ProductStatus;

@Mapper(componentModel = "spring")
public interface ProductMapping {
    @Mapping(source = "productName", target = "productName", qualifiedByName = "mapProductNameToString")
    @Mapping(source = "productPhoto", target = "productPhoto", qualifiedByName = "mapProductPhotoToString")
    @Mapping(source = "productDescription", target = "productDescription", qualifiedByName = "mapProductDescriptionToString")
    @Mapping(source = "productPrice", target = "productPrice", qualifiedByName = "mapProductPriceToBigDecimal")
    @Mapping(source = "productSku", target = "sku", qualifiedByName = "mapProductSkuToString")
    @Mapping(source = "status", target = "status", qualifiedByName = "mapProductStatusToString")
    ProductDto toDto(Product product);
    List<ProductDto> toDtoList(List<Product> products);

    @Named("mapProductNameToString")
    default String mapProductNameToString(ProductName productName) {
        return productName.productName();
    }

    @Named("mapProductDescriptionToString") 
    default String mapProductDescriptionToString(ProductDescription productDescription) {
        return productDescription.desc();
    }

    @Named("mapProductSkuToString")
    default String mapProdcutSkuToString(ProductSku productSku) {
        return productSku.value();
    }

    @Named("mapProductPriceToBigDecimal")  
    default BigDecimal mapProdcutPriceToString(ProductPrice productPrice) {
        return productPrice.price();
    }

    @Named("mapProductStatusToString")
    default String mapProductStatusToString(ProductStatus productStatus) {
        return productStatus.name();
    }

    @Named ("mapProductPhotoToString") 
    default String mapProductPhotoToString(ProductPhoto productPhoto) {
        return productPhoto.filePath();
    }
}
