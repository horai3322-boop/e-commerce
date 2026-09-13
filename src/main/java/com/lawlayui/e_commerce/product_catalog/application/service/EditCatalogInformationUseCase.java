package com.lawlayui.e_commerce.product_catalog.application.service;

import org.springframework.stereotype.Service;

import com.lawlayui.e_commerce.product_catalog.application.event.ProductPhotoChangedEvent;
import com.lawlayui.e_commerce.product_catalog.application.exception.CatalogNotFoundException;
import com.lawlayui.e_commerce.product_catalog.application.port.in.EditCatalogInformationCommand;
import com.lawlayui.e_commerce.product_catalog.application.port.out.EventPublisher;
import com.lawlayui.e_commerce.product_catalog.application.port.out.ProductCatalogRepository;
import com.lawlayui.e_commerce.product_catalog.domain.model.Product;
import com.lawlayui.e_commerce.product_catalog.domain.value_object.ProductDescription;
import com.lawlayui.e_commerce.product_catalog.domain.value_object.ProductName;
import com.lawlayui.e_commerce.product_catalog.domain.value_object.ProductPhoto;
import com.lawlayui.e_commerce.product_catalog.domain.value_object.ProductPrice;

@Service
public class EditCatalogInformationUseCase{
    private final ProductCatalogRepository productRepository;
    private final EventPublisher eventPublisher;    

    public EditCatalogInformationUseCase(ProductCatalogRepository productRepository, EventPublisher eventPublisher) {
        this.productRepository = productRepository;
        this.eventPublisher = eventPublisher;
    }

    public void execute(EditCatalogInformationCommand command) {
        Product product = productRepository.getBySku(command.sku())
            .orElseThrow(() -> new CatalogNotFoundException(command.sku()));

        if (command.name() != null) {
            product.changeProductName(new ProductName(command.name()));
        }
        if (command.description() != null) {
            product.changeProductDescription(new ProductDescription(command.description()));
        }
        if (command.price() != null) {
            product.changeProductPrice(new ProductPrice(command.price()));
        }
        if (command.photo() != null) {
            String oldPhoto = product.getProductPhoto().filePath();
            product.changeProductPhoto(new ProductPhoto(command.photo()));
            eventPublisher.publish(new ProductPhotoChangedEvent(product.getProductSku().value(), oldPhoto, command.photo()));
        }

        productRepository.save(product);
    }
}
