package com.lawlayui.e_commerce.product_catalog.domain.model;

import java.io.ObjectInputFilter.Status;

import com.lawlayui.e_commerce.product_catalog.domain.value_object.ProductDescription;
import com.lawlayui.e_commerce.product_catalog.domain.value_object.ProductId;
import com.lawlayui.e_commerce.product_catalog.domain.value_object.ProductName;
import com.lawlayui.e_commerce.product_catalog.domain.value_object.ProductPhoto;
import com.lawlayui.e_commerce.product_catalog.domain.value_object.ProductPrice;

public class Product {
    private ProductId productId;
    private ProductName productName;
    private ProductPhoto productPhoto;
    private ProductDescription productDescription;
    private ProductPrice productPrice; 
    private Status status;

    public void updateInfo(ProductName productName, ProductPhoto productPhoto, ProductDescription productDescription, ProductPrice productPrice, Status status) {
        this.productName = productName;
        this.productPhoto = productPhoto;
        this.productDescription = productDescription;
        this.productPrice = productPrice;
        this.status = status;
    }

    public ProductId getProductId() {return productId;}
    public ProductName getProductName() {return productName;}
    public ProductDescription getProductDescription() {return productDescription;}
    public ProductPrice getProductPrice() {return productPrice;}
    public ProductPhoto getProductPhoto() {return productPhoto;}
    public Status getStatus() {return status;}
}
