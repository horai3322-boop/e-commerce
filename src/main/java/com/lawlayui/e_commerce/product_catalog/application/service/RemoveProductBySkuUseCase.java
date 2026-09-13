package com.lawlayui.e_commerce.product_catalog.application.service;

import org.springframework.stereotype.Service;

import com.lawlayui.e_commerce.product_catalog.application.event.ProductRemovedEvent;
import com.lawlayui.e_commerce.product_catalog.application.exception.CatalogNotFoundException;
import com.lawlayui.e_commerce.product_catalog.application.port.in.RemoveProductBySkuCommand;
import com.lawlayui.e_commerce.product_catalog.application.port.out.EventPublisher;
import com.lawlayui.e_commerce.product_catalog.application.port.out.ProductCatalogRepository;
import com.lawlayui.e_commerce.product_catalog.domain.model.Product;

@Service 
public class RemoveProductBySkuUseCase{
    private final ProductCatalogRepository repository;
    private final EventPublisher eventPublisher;

    public RemoveProductBySkuUseCase(ProductCatalogRepository repository, EventPublisher eventPublisher) {
        this.repository = repository;
        this.eventPublisher = eventPublisher;
    }
    
    public void execute(RemoveProductBySkuCommand command) {
        Product product = repository.getBySku(command.sku())    
            .orElseThrow(() -> new CatalogNotFoundException("sku"));

        product.remove();
        repository.save(product);
        eventPublisher.publish(new ProductRemovedEvent(product.getProductSku().value(), product.getStatus().name()));
    }
}
