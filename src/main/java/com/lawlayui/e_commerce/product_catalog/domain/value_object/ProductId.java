package com.lawlayui.e_commerce.product_catalog.domain.value_object;

import java.util.UUID;
import java.util.Objects;


public record ProductId(String id) {
    public ProductId {
        Objects.requireNonNull(id, "ProductId cannot be null");
    }

    public static ProductId generateId() {
        return new ProductId(UUID.randomUUID().toString());
    }
}
