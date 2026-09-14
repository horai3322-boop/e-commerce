package com.lawlayui.e_commerce.product_inventory.infrastructure.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Table(name="inventory_items")
@Entity
public class InventoryItemJpaEntity {
    public InventoryItemJpaEntity() {}
    public InventoryItemJpaEntity(String id, String sku, int availableStock, int reservedStock, String locationCode) {
        this.id = id;
        this.sku = sku;
        this.availableStock = availableStock;
        this.reservedStock = reservedStock;
        this.locationCode = locationCode;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getSku() {
        return sku;
    }
    public void setSku(String sku) {
        this.sku = sku;
    }
    public int getAvailableStock() {
        return availableStock;
    }
    public void setAvailableStock(int availableStock) {
        this.availableStock = availableStock;
    }
    public int getReservedStock() {
        return reservedStock;
    }
    public void setReservedStock(int reservedStock) {
        this.reservedStock = reservedStock;
    }
    public String getLocationCode() {
        return locationCode;
    }
    public void setLocationCode(String locationCode) {
        this.locationCode = locationCode;
    }
    @Id 
    private String id;
    @Column(nullable = false, unique = true, length = 255)
    private String sku;
    @Column(name = "available_quantity", nullable = false)
    private Integer availableStock;
    @Column(name = "reserved_quantity", nullable = false)
    private Integer reservedStock;
    @Column(name = "location_code", nullable = false)
    private String locationCode;
}
