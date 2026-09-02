package com.lawlayui.e_commerce.product_inventory.application.service;

import org.springframework.stereotype.Service;

import com.lawlayui.e_commerce.product_inventory.application.exception.InventoryItemNotFound;
import com.lawlayui.e_commerce.product_inventory.application.port.in.ReserveStockCommand;
import com.lawlayui.e_commerce.product_inventory.application.port.in.ReserveStockUseCase;
import com.lawlayui.e_commerce.product_inventory.application.port.out.InventoryRepository;
import com.lawlayui.e_commerce.product_inventory.domain.model.InventoryItem;
import com.lawlayui.e_commerce.product_inventory.domain.value_object.StockQuantitiy;

@Service
public class ReserveStockImpl implements ReserveStockUseCase{
    private final InventoryRepository inventoryRepository;

    public ReserveStockImpl(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    public void execute(ReserveStockCommand command) {
        InventoryItem inventoryItem = inventoryRepository.findBySKU(command.sku())
            .orElseThrow(() -> new InventoryItemNotFound(command.sku()));

        inventoryItem.reserveStock(new StockQuantitiy(command.quantity()));
        inventoryRepository.save(inventoryItem);
    }
}
