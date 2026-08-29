package com.lawlayui.e_commerce.product_catalog.domain.model;

import java.util.Objects;
import java.math.BigDecimal;

import com.lawlayui.e_commerce.product_catalog.domain.value_object.ProductDescription;
import com.lawlayui.e_commerce.product_catalog.domain.value_object.ProductId;
import com.lawlayui.e_commerce.product_catalog.domain.value_object.ProductName;
import com.lawlayui.e_commerce.product_catalog.domain.value_object.ProductPhoto;
import com.lawlayui.e_commerce.product_catalog.domain.value_object.ProductPrice;
import com.lawlayui.e_commerce.product_catalog.domain.value_object.ProductStatus;


public class Product {
    private ProductId productId;
    private ProductName productName;
    private ProductPhoto productPhoto;
    private ProductDescription productDescription;
    private ProductPrice productPrice; 
    private ProductStatus status;
    private boolean hasTransactionHistory;

    public static Product create(
        ProductId productId,
        ProductName name,
        ProductPhoto photo,
        ProductDescription description,
        ProductPrice price,
        int initialStock
    ) {
        Product product = new Product();
        product.productId = Objects.requireNonNull(productId, "ProductId cannot be null");
        product.productName = Objects.requireNonNull(name, "ProductName cannot be null");
        product.productPhoto = Objects.requireNonNull(photo, "ProductPhoto cannot be null");
        product.productDescription = Objects.requireNonNull(description, "ProductDescription cannot be null");
        product.productPrice = Objects.requireNonNull(price, "ProductPrice cannot be null");

        product.status = (initialStock > 0) ? ProductStatus.AVALIABLE : ProductStatus.NOT_AVALIABLE;

        return product;
    }

    public void remove() {
        if (hasTransactionHistory) {
            this.status = ProductStatus.ARCHIVED;
        } else {
            this.status = ProductStatus.PENDING_DELETION;
        }
    }

    public void markAsTransacted() {
        this.hasTransactionHistory = true;
    }

    public void changeProductName(ProductName productName) {
        if (productName.productName().length() > 255) {
            throw new IllegalArgumentException("product name exceeds maximum length of 255 characters");
        }
        if (productName.productName().length() < 1) {
            throw new IllegalArgumentException("product name cannot be empty");
        }
        Objects.requireNonNull(productName, "ProductName cannot be null");
        this.productName = productName;
    }

    public void changeProductPhoto(ProductPhoto productPhoto) {
        Objects.requireNonNull(productPhoto, "ProductPhoto cannot be null");
        this.productPhoto = productPhoto;
    }

    public void changeProductDescription(ProductDescription productDescription) {
        if (productDescription.desc().length() > 1000) {
            throw new IllegalArgumentException("product description exceeds maximum length of 1000 characters");
        }
        if (productDescription.desc().length() < 1) {
            throw new IllegalArgumentException("product description cannot be empty");
        }
        Objects.requireNonNull(productDescription, "ProductDescription cannot be null");
        this.productDescription = productDescription;
    }

    public void changeProductPrice(ProductPrice productPrice) {
        Objects.requireNonNull(productPrice, "ProductPrice cannot be null");
        if (productPrice.price().compareTo(BigDecimal.ZERO) == 0) {
            throw new IllegalArgumentException("Product price cannot be zero");
        }
        this.productPrice = productPrice;
    }

    public void changeStatus(ProductStatus status) {
        Objects.requireNonNull(status, "Status cannot be null");
        this.status = status;
    }

    public ProductId getProductId() {return productId;}
    public ProductName getProductName() {return productName;}
    public ProductDescription getProductDescription() {return productDescription;}
    public ProductPrice getProductPrice() {return productPrice;}
    public ProductPhoto getProductPhoto() {return productPhoto;}
    public ProductStatus getStatus() {return status;}
}