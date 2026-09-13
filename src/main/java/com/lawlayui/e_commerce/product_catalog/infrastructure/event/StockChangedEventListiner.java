package com.lawlayui.e_commerce.product_catalog.infrastructure.event;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.lawlayui.e_commerce.product_catalog.application.port.in.ChangeCatalogStatusCommand;
import com.lawlayui.e_commerce.product_catalog.application.service.ChangeCatalogStatusUseCase;
import com.lawlayui.e_commerce.product_inventory.application.event.StockDeductedEvent;

@Component 
public class StockChangedEventListiner {
    private final ChangeCatalogStatusUseCase changeCatalogStatusUseCase;

    public StockChangedEventListiner(ChangeCatalogStatusUseCase changeCatalogStatusUseCase) {
        this.changeCatalogStatusUseCase = changeCatalogStatusUseCase;
    }

    @EventListener 
    public void handleStackChanged(StockDeductedEvent event) {
        changeCatalogStatusUseCase.execute(new ChangeCatalogStatusCommand(event.sku(), event.quantity()));
    }
}
