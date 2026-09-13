package com.lawlayui.e_commerce.product_inventory.application.port.in;

public interface GetInventoryItemUseCase {
    InventoryItemDto execute(GetInventoryItemQuery query);
}
