package com.lawlayui.e_commerce.product_inventory.application.service;

import org.springframework.stereotype.Service;

import com.lawlayui.e_commerce.product_catalog.application.port.out.EventPublisher;
import com.lawlayui.e_commerce.product_inventory.application.event.StockRelocatedEvent;
import com.lawlayui.e_commerce.product_inventory.application.exception.InventoryItemNotFound;
import com.lawlayui.e_commerce.product_inventory.application.port.in.RelocateStockCommand;
import com.lawlayui.e_commerce.product_inventory.application.port.in.RelocateStockUseCase;
import com.lawlayui.e_commerce.product_inventory.application.port.out.InventoryRepository;
import com.lawlayui.e_commerce.product_inventory.domain.model.InventoryItem;
import com.lawlayui.e_commerce.product_inventory.domain.value_object.LocationCode;

@Service
public class RelocateStockImpl implements RelocateStockUseCase {
    private final InventoryRepository inventoryRepository;
    private final EventPublisher eventPublisher;

    public RelocateStockImpl(InventoryRepository inventoryRepository, EventPublisher eventPublisher) {
        this.inventoryRepository = inventoryRepository;
        this.eventPublisher = eventPublisher;
    }
    public void execute(RelocateStockCommand command) {
        InventoryItem inventoryItem = inventoryRepository.findBySKU(command.sku())
            .orElseThrow(() -> new InventoryItemNotFound(command.sku()));

        LocationCode oldLocationCode = inventoryItem.getLocationCode();
        inventoryItem.relocate(new LocationCode(command.newLocationCode()));
        inventoryRepository.save(inventoryItem);
        eventPublisher.publish(new StockRelocatedEvent(inventoryItem.getSku().value(), oldLocationCode.value(), inventoryItem.getLocationCode().value(), inventoryItem.getAvailableStock().value()));
    }
}
