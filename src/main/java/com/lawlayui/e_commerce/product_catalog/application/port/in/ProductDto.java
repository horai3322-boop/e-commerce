package com.lawlayui.e_commerce.product_catalog.application.port.in;

import com.lawlayui.e_commerce.product_catalog.domain.value_object.ProductDescription;
import com.lawlayui.e_commerce.product_catalog.domain.value_object.ProductId;
import com.lawlayui.e_commerce.product_catalog.domain.value_object.ProductName;
import com.lawlayui.e_commerce.product_catalog.domain.value_object.ProductPhoto;
import com.lawlayui.e_commerce.product_catalog.domain.value_object.ProductPrice;

public record ProductDto(ProductId productId, ProductName productName, ProductDescription productDescription, ProductPrice productPrice, ProductPhoto filePath) {
    
}
