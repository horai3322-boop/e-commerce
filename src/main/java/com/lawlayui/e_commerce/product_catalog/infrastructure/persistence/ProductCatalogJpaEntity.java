package com.lawlayui.e_commerce.product_catalog.infrastructure.persistence;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "catalog_products")
public class ProductCatalogJpaEntity {
    @Id
    private String productId;
    @Column(name = "name", nullable = false, length = 255)
    private String productName;
    @Column(name = "photo", length = 500)
    private String productPhoto;
    @Column(name = "description", nullable = false, length = 1000)
    private String productDescription;
    @Column(name = "price", nullable = false, precision = 19, scale = 4)
    private BigDecimal productPrice;
    @Column(name = "status", nullable = false, length = 50)
    private String status;
    public ProductCatalogJpaEntity(String productId, String productName, String productPhoto, String productDescription,
            BigDecimal productPrice, String status) {
        this.productId = productId;
        this.productName = productName;
        this.productPhoto = productPhoto;
        this.productDescription = productDescription;
        this.productPrice = productPrice;
        this.status = status;
    }
    public String getProductId() {
        return productId;
    }
    public String getProductName() {
        return productName;
    }
    public String getProductPhoto() {
        return productPhoto;
    }
    public String getProductDescription() {
        return productDescription;
    }
    public BigDecimal getProductPrice() {
        return productPrice;
    }
    public String getStatus() {
        return status;
    }
    public void setProductId(String productId) {
        this.productId = productId;
    }
    public void setProductName(String productName) {
        this.productName = productName;
    }
    public void setProductPhoto(String productPhoto) {
        this.productPhoto = productPhoto;
    }
    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }
    public void setProductPrice(BigDecimal productPrice) {
        this.productPrice = productPrice;
    }
    public void setStatus(String status) {
        this.status = status;
    }
}
