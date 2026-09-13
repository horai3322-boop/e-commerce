package com.lawlayui.e_commerce.product_catalog.application.service;

import org.springframework.stereotype.Service;

import com.lawlayui.e_commerce.product_catalog.application.event.ProductCreatedEvent;
import com.lawlayui.e_commerce.product_catalog.application.mapper.ProductMapping;
import com.lawlayui.e_commerce.product_catalog.application.port.in.AddProductCommand;
import com.lawlayui.e_commerce.product_catalog.application.port.in.ProductDto;
import com.lawlayui.e_commerce.product_catalog.application.port.out.EventPublisher;
import com.lawlayui.e_commerce.product_catalog.application.port.out.ProductCatalogRepository;
import com.lawlayui.e_commerce.product_catalog.domain.model.Product;
import com.lawlayui.e_commerce.product_catalog.domain.value_object.*;;

@Service
public class AddProductUseCase{
    private final ProductCatalogRepository productRepository;
    private final ProductMapping productMapping;
    private final EventPublisher eventPublisher;

    public AddProductUseCase(ProductCatalogRepository productRepository, ProductMapping mapping, EventPublisher eventPublisher) {
        this.productRepository = productRepository;
        this.productMapping = mapping;
        this.eventPublisher = eventPublisher;
    }

    public ProductDto execute(AddProductCommand command) {
        Product product = Product.create(
            new ProductName(command.name()),
            new ProductPhoto(command.photo()),
            new ProductDescription(command.description()),
            new ProductPrice(command.price()),
            0,
            new ProductSku(command.sku())
        );

        productRepository.save(product);
        eventPublisher.publish(new ProductCreatedEvent(product.getProductSku().value()));
        return productMapping.toDto(product);
    }
    
}
