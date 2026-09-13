package com.lawlayui.e_commerce.product_catalog.infrastructure.persistence;

import java.math.BigDecimal;

import org.springframework.data.domain.Persistable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PostLoad;
import jakarta.persistence.PostPersist;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "catalog_products")
public class ProductCatalogJpaEntity implements Persistable<String>{
    @Id 
    @Column(name = "sku", nullable = false, unique = true, length = 1000)
    private String sku;
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

    @Transient  
    private boolean isNew = true;    

    @PostLoad 
    @PostPersist 
    public void markNotNew() {
        this.isNew = false;
    }

    public ProductCatalogJpaEntity() {}
    public ProductCatalogJpaEntity(String productName, String productPhoto, String productDescription,
            BigDecimal productPrice, String status, String sku) {
        this.productName = productName;
        this.productPhoto = productPhoto;
        this.productDescription = productDescription;
        this.productPrice = productPrice;
        this.status = status;
        this.sku = sku;
    }
    @Override 
    public boolean isNew() {
        return this.isNew;
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
    @Override 
    public String getId() {
        return sku;
    }
    public String getSku() {
        return sku;
    }
    public void setSku(String sku) {
        this.sku = sku;
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
