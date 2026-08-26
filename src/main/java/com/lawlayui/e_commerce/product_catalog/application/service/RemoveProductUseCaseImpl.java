package com.lawlayui.e_commerce.product_catalog.application.service;


import org.springframework.stereotype.Service;

import com.lawlayui.e_commerce.product_catalog.application.event.ProductRemovedEvent;
import com.lawlayui.e_commerce.product_catalog.application.exception.ProductNotFoundException;
import com.lawlayui.e_commerce.product_catalog.application.port.in.RemoveProductCommand;
import com.lawlayui.e_commerce.product_catalog.application.port.in.RemoveProductUseCase;
import com.lawlayui.e_commerce.product_catalog.application.port.out.EventPublisher;
import com.lawlayui.e_commerce.product_catalog.application.port.out.ProductCatalogRepository;
import com.lawlayui.e_commerce.product_catalog.domain.model.Product;
import com.lawlayui.e_commerce.product_catalog.domain.value_object.ProductId;

@Service
public class RemoveProductUseCaseImpl implements RemoveProductUseCase {
    private final ProductCatalogRepository productCatalogRepository;
    private final EventPublisher eventPublisher;

    public RemoveProductUseCaseImpl(ProductCatalogRepository productCatalogRepository, EventPublisher eventPublisher) {
        this.productCatalogRepository = productCatalogRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void execute(RemoveProductCommand command) {
        Product product = productCatalogRepository.getById(new ProductId(command.productId()))
            .orElseThrow(() -> new ProductNotFoundException(command.productId()));

        product.remove();
        eventPublisher.publish(new ProductRemovedEvent(command.productId()));
    }
    
}
