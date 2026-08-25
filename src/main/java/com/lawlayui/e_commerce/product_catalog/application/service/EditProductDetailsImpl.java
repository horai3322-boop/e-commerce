package com.lawlayui.e_commerce.product_catalog.application.service;

import com.lawlayui.e_commerce.product_catalog.application.event.ProductPhotoChangedEvent;
import com.lawlayui.e_commerce.product_catalog.application.exception.ProductNotFoundException;
import com.lawlayui.e_commerce.product_catalog.application.port.in.EditCatalogInformationCommand;
import com.lawlayui.e_commerce.product_catalog.application.port.in.EditCatalogInformationUseCase;
import com.lawlayui.e_commerce.product_catalog.application.port.out.EventPublisher;
import com.lawlayui.e_commerce.product_catalog.application.port.out.ProductCatalogRepository;
import com.lawlayui.e_commerce.product_catalog.domain.model.Product;
import com.lawlayui.e_commerce.product_catalog.domain.value_object.ProductDescription;
import com.lawlayui.e_commerce.product_catalog.domain.value_object.ProductId;
import com.lawlayui.e_commerce.product_catalog.domain.value_object.ProductName;
import com.lawlayui.e_commerce.product_catalog.domain.value_object.ProductPhoto;
import com.lawlayui.e_commerce.product_catalog.domain.value_object.ProductPrice;

public class EditProductDetailsImpl implements EditCatalogInformationUseCase {
    private final ProductCatalogRepository productRepository;
    private final EventPublisher eventPublisher;    

    public EditProductDetailsImpl(ProductCatalogRepository productRepository, EventPublisher eventPublisher) {
        this.productRepository = productRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void execute(EditCatalogInformationCommand command) {
        Product product = productRepository.getById(new ProductId(command.productId()))
            .orElseThrow(() -> new ProductNotFoundException(command.productId()));

        if (command.name() != null) {
            product.changeProductName(new ProductName(command.name()));
        }
        if (command.description() != null) {
            product.changeProductDescription(new ProductDescription(command.description()));
        }
        if (command.price() != null) {
            product.changeProductPrice(new ProductPrice(command.price()));
        }
        if (command.filePath() != null) {
            String oldPhoto = product.getProductPhoto().filePath();
            product.changeProductPhoto(new ProductPhoto(command.filePath()));
            eventPublisher.publish(new ProductPhotoChangedEvent(product.getProductId().id(), oldPhoto, command.filePath()));
        }
        if (command.filePath() == null ) {
            String oldPhoto = product.getProductPhoto().filePath();
            product.changeProductPhoto(new ProductPhoto(command.filePath()));
            eventPublisher.publish(new ProductPhotoChangedEvent(product.getProductId().id(), oldPhoto, command.filePath()));
        }
        else {
            return;
        }
        productRepository.save(product);
    }
}
