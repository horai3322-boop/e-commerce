package com.lawlayui.e_commerce.product_inventory.application.service;

import org.springframework.stereotype.Service;

import com.lawlayui.e_commerce.product_inventory.application.exception.InventoryItemNotFound;
import com.lawlayui.e_commerce.product_inventory.application.port.in.ReplenishStockCommand;
import com.lawlayui.e_commerce.product_inventory.application.port.in.ReplenishStockUseCase;
import com.lawlayui.e_commerce.product_inventory.application.port.out.InventoryRepository;
import com.lawlayui.e_commerce.product_inventory.domain.model.InventoryItem;
import com.lawlayui.e_commerce.product_inventory.domain.value_object.StockQuantitiy;

@Service
public class ReplenishStockImpl implements ReplenishStockUseCase{
    private final InventoryRepository inventoryRepository;

    public ReplenishStockImpl(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    public void execute(ReplenishStockCommand command) {
        InventoryItem inventoryItem = inventoryRepository.findBySKU(command.sku())
            .orElseThrow(() -> new InventoryItemNotFound(command.sku()));
        inventoryItem.replenishStock(new StockQuantitiy(command.quantity()));
        inventoryRepository.save(inventoryItem);
    }
}
