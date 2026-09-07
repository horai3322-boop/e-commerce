package com.lawlayui.e_commerce.product_inventory.application.service;

import org.springframework.stereotype.Service;

import com.lawlayui.e_commerce.product_catalog.application.port.out.EventPublisher;
import com.lawlayui.e_commerce.product_inventory.application.event.InventoryCreatedEvent;
import com.lawlayui.e_commerce.product_inventory.application.port.in.CreateInventoryCommand;
import com.lawlayui.e_commerce.product_inventory.application.port.in.CreateInventoryUseCase;
import com.lawlayui.e_commerce.product_inventory.application.port.out.InventoryRepository;
import com.lawlayui.e_commerce.product_inventory.domain.model.InventoryItem;
import com.lawlayui.e_commerce.product_inventory.domain.value_object.InventoryItemId;
import com.lawlayui.e_commerce.product_inventory.domain.value_object.LocationCode;
import com.lawlayui.e_commerce.product_inventory.domain.value_object.SKU;
import com.lawlayui.e_commerce.product_inventory.domain.value_object.StockQuantitiy;

@Service
public class CreateInventoryImpl implements CreateInventoryUseCase{
    private final InventoryRepository inventoryRepository;
    private final EventPublisher eventPublisher;


    public CreateInventoryImpl(InventoryRepository inventoryRepository, EventPublisher eventPublisher) {
        this.inventoryRepository = inventoryRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void execute(CreateInventoryCommand command) {
        InventoryItem inventoryItem =InventoryItem.create(InventoryItemId.generateId(), new SKU(command.sku()), new StockQuantitiy(command.quantity()), new LocationCode(command.locationCode()));

        inventoryRepository.save(inventoryItem);
        eventPublisher.publish(new InventoryCreatedEvent(
            inventoryItem.getId().value(),
            inventoryItem.getSku().value(),
            inventoryItem.getAvailableStock().value(),
            inventoryItem.getLocationCode().value()
        ));
    }
}
