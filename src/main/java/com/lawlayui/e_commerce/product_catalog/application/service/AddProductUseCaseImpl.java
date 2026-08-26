package com.lawlayui.e_commerce.product_catalog.application.service;

import org.springframework.stereotype.Service;

import com.lawlayui.e_commerce.product_catalog.application.event.ProductCreatedEvent;
import com.lawlayui.e_commerce.product_catalog.application.mapper.ProductMapping;
import com.lawlayui.e_commerce.product_catalog.application.port.in.AddProductCommand;
import com.lawlayui.e_commerce.product_catalog.application.port.in.AddProductUseCase;
import com.lawlayui.e_commerce.product_catalog.application.port.in.ProductDto;
import com.lawlayui.e_commerce.product_catalog.application.port.out.EventPublisher;
import com.lawlayui.e_commerce.product_catalog.application.port.out.ProductCatalogRepository;
import com.lawlayui.e_commerce.product_catalog.domain.model.Product;
import com.lawlayui.e_commerce.product_catalog.domain.value_object.*;;

@Service
public class AddProductUseCaseImpl implements AddProductUseCase {
    private final ProductCatalogRepository productRepository;
    private final ProductMapping productMapping;
    private final EventPublisher eventPublisher;

    public AddProductUseCaseImpl(ProductCatalogRepository productRepository, ProductMapping mapping, EventPublisher eventPublisher) {
        this.productRepository = productRepository;
        this.productMapping = mapping;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public ProductDto execute(AddProductCommand command) {
        Product product = Product.create(
            ProductId.generateId(),
            new ProductName(command.name()),
            new ProductPhoto(command.file_path()),
            new ProductDescription(command.descirption()),
            new ProductPrice(command.price()),
            command.initialStock()
        );

        productRepository.save(product);
        eventPublisher.publish(new ProductCreatedEvent(product.getProductId().id(), command.initialStock()));
        return productMapping.toDto(product);
    }
    
}
