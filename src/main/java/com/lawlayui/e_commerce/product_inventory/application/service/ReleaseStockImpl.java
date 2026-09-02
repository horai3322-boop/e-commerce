package com.lawlayui.e_commerce.product_inventory.application.service;

import org.springframework.stereotype.Service;

import com.lawlayui.e_commerce.product_catalog.application.port.out.EventPublisher;
import com.lawlayui.e_commerce.product_inventory.application.event.StockReleasedEvent;
import com.lawlayui.e_commerce.product_inventory.application.exception.InventoryItemNotFound;
import com.lawlayui.e_commerce.product_inventory.application.port.in.ReleaseStockCommand;
import com.lawlayui.e_commerce.product_inventory.application.port.in.ReleaseStockUseCase;
import com.lawlayui.e_commerce.product_inventory.application.port.out.InventoryRepository;
import com.lawlayui.e_commerce.product_inventory.domain.model.InventoryItem;
import com.lawlayui.e_commerce.product_inventory.domain.value_object.StockQuantitiy;

@Service
public class ReleaseStockImpl implements ReleaseStockUseCase {
    private final InventoryRepository inventoryRepository;
    private final EventPublisher eventPublisher;

    public ReleaseStockImpl(InventoryRepository inventoryRepository, EventPublisher eventPublisher) {
        this.inventoryRepository = inventoryRepository;
        this.eventPublisher = eventPublisher;
    }

    public void execute(ReleaseStockCommand command) {
        InventoryItem inventoryItem = inventoryRepository.findBySKU(command.sku())
            .orElseThrow(() -> new InventoryItemNotFound(command.sku()));

        inventoryItem.releaseReservedStock(new StockQuantitiy(command.quantity()));

        inventoryRepository.save(inventoryItem);
        eventPublisher.publish(new StockReleasedEvent(command.sku(), command.quantity()));
    }
}
