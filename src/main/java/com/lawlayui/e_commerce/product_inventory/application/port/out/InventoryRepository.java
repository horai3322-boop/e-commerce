package com.lawlayui.e_commerce.product_inventory.application.port.out;

import java.util.Optional;

import com.lawlayui.e_commerce.product_inventory.domain.model.InventoryItem;

public interface InventoryRepository {
    void save(InventoryItem inventoryItem);
    Optional<InventoryItem> findBySKU(String sku);
}
