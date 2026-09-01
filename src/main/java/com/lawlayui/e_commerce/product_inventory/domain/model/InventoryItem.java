package com.lawlayui.e_commerce.product_inventory.domain.model;

import com.lawlayui.e_commerce.product_inventory.domain.value_object.InventoryItemId;
import com.lawlayui.e_commerce.product_inventory.domain.value_object.LocationCode;
import com.lawlayui.e_commerce.product_inventory.domain.value_object.SKU;
import com.lawlayui.e_commerce.product_inventory.domain.value_object.StockQuantitiy;
import com.lawlayui.e_commerce.product_inventory.domain.exception.*;

public class InventoryItem {
    private InventoryItemId id;
    private SKU sku;
    private StockQuantitiy avaliableStock;
    private StockQuantitiy reservedStock;
    private LocationCode locationCode;

    public static InventoryItem create(InventoryItemId id, SKU sku, StockQuantitiy avaliableStock, LocationCode locationCode) {
        InventoryItem inventoryItem = new InventoryItem();
        if (id == null) {
            throw new IllegalArgumentException("InventoryItemId cannot be null");
        }
        if (sku == null) {
            throw new IllegalArgumentException("SKU cannot be null");
        }
        if (avaliableStock == null) {
            throw new IllegalArgumentException("StockQuantity cannot be null");
        }
        if (locationCode == null) {
            throw new IllegalArgumentException("LocationCode cannot be null");
        }
        inventoryItem.id = id;
        inventoryItem.sku = sku;
        inventoryItem.avaliableStock = avaliableStock;
        inventoryItem.locationCode = locationCode;
        inventoryItem.reservedStock = new StockQuantitiy(0);
        return inventoryItem;
    }

    public void relocate(LocationCode newLocationCode) {
        if (newLocationCode == null) {
            throw new IllegalArgumentException("New LocationCode cannot be null");
        }
        this.locationCode = newLocationCode;
    }

    public void deductReservedStock(StockQuantitiy quantityToDeduct) {
        if (quantityToDeduct == null) {
            throw new IllegalArgumentException("Quantity to deduct cannot be null");
        }
        if (this.reservedStock.value() < quantityToDeduct.value()) {
            throw new InsufficientStockException();
        }
        this.reservedStock = new StockQuantitiy(this.reservedStock.value() - quantityToDeduct.value());
    }

    public void replenishStock(StockQuantitiy quantityToReplenish) {
        if (quantityToReplenish == null) {
            throw new IllegalArgumentException("Quantity to replenish cannot be null");
        }
        this.avaliableStock = new StockQuantitiy(this.avaliableStock.value() + quantityToReplenish.value());
    }

    public void reserveStock(StockQuantitiy quantityToReserve) {
        if (quantityToReserve == null) {
            throw new IllegalArgumentException("Quantity to reserve cannot be null");
        }
        if (this.avaliableStock.value() < quantityToReserve.value()) {
            throw new InsufficientStockException();
        }
        this.avaliableStock = new StockQuantitiy(this.avaliableStock.value() - quantityToReserve.value());
        this.reservedStock = new StockQuantitiy(this.reservedStock.value() + quantityToReserve.value());
    }

    public void releaseReservedStock(StockQuantitiy quantityToRelease) {
        if (quantityToRelease == null) {
            throw new IllegalArgumentException("Quantity to release cannot be null");
        }
        if (this.reservedStock.value() < quantityToRelease.value()) {
            throw new InsufficientStockException();
        }
        this.reservedStock = new StockQuantitiy(this.reservedStock.value() - quantityToRelease.value());
        this.avaliableStock = new StockQuantitiy(this.avaliableStock.value() + quantityToRelease.value());
    }
    public InventoryItemId getId() {
        return id;
    }

    public SKU getSku() {
        return sku;
    }

    public StockQuantitiy getAvaliableStock() {
        return avaliableStock;
    }

    public StockQuantitiy getReservedStock() {
        return reservedStock;
    }

    public LocationCode getLocationCode() {
        return locationCode;
    }
}
