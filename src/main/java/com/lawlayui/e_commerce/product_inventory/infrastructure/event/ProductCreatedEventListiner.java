package com.lawlayui.e_commerce.product_inventory.infrastructure.event;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.lawlayui.e_commerce.product_catalog.application.event.ProductCreatedEvent;
import com.lawlayui.e_commerce.product_inventory.application.port.in.CreateInventoryCommand;
import com.lawlayui.e_commerce.product_inventory.application.port.in.CreateInventoryUseCase;

@Component 
public class ProductCreatedEventListiner {
    private final CreateInventoryUseCase createInventoryUseCase;

    public ProductCreatedEventListiner(CreateInventoryUseCase createInventoryUseCase) {
        this.createInventoryUseCase = createInventoryUseCase;
    }

    @EventListener 
    public void handleProductCreated(ProductCreatedEvent event) {
        createInventoryUseCase.execute(new CreateInventoryCommand(event.sku(), 0, "UNASSIGNED-DEFAULT-RACK00-H0-B0"));
    }
}
