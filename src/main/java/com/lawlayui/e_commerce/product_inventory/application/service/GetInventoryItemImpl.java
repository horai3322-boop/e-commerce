package com.lawlayui.e_commerce.product_inventory.application.service;

import org.springframework.stereotype.Service;

import com.lawlayui.e_commerce.product_inventory.application.exception.InventoryItemNotFound;
import com.lawlayui.e_commerce.product_inventory.application.mapper.InventoryItemMapping;
import com.lawlayui.e_commerce.product_inventory.application.port.in.GetInventoryItemQuery;
import com.lawlayui.e_commerce.product_inventory.application.port.in.GetInventoryItemUseCase;
import com.lawlayui.e_commerce.product_inventory.application.port.in.InventoryItemDto;
import com.lawlayui.e_commerce.product_inventory.application.port.out.InventoryRepository;
import com.lawlayui.e_commerce.product_inventory.domain.model.InventoryItem;

@Service 
public class GetInventoryItemImpl implements  GetInventoryItemUseCase {
    private final InventoryRepository inventoryRepository;
    private final InventoryItemMapping inventoryItemMapping;

    public GetInventoryItemImpl(InventoryRepository inventoryRepository, InventoryItemMapping inventoryItemMapping) {
        this.inventoryRepository = inventoryRepository;
        this.inventoryItemMapping = inventoryItemMapping;
    }

    @Override
    public InventoryItemDto execute(GetInventoryItemQuery query) {
        InventoryItem inventoryItem = inventoryRepository.findBySKU(query.sku())
                .orElseThrow(() -> new InventoryItemNotFound(query.sku()));
        return inventoryItemMapping.toDto(inventoryItem);
    }
}
