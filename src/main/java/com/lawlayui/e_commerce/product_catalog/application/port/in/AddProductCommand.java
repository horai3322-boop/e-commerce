package com.lawlayui.e_commerce.product_catalog.application.port.in;

import java.math.BigDecimal;
import java.util.Objects;

public record AddProductCommand(
    String name, 
    String description, 
    BigDecimal price, 
    String photo, 
    String sku
) {
    public AddProductCommand {
        Objects.requireNonNull(name, "Name cannot be null");
        Objects.requireNonNull(description, "description cannot be null");
        Objects.requireNonNull(price, "Price cannot be null");
        Objects.requireNonNull(photo, "Photo cannot be null");
        Objects.requireNonNull(sku, "SKU cannot be null");

        if (name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be blank");
        }
        if (name.length() < 3 || name.length() > 255) {
            throw new IllegalArgumentException("The character name must be between 3 and 255 characters long");
        }
        if (description.isBlank()) {
            throw new IllegalArgumentException("description cannot be blank");
        }
        if (description.length() < 50 || description.length() > 1000) {
            throw new IllegalArgumentException("The character description must be between 50 and 1000 characters long");
        }
        if (photo.isBlank()) {
            throw new IllegalArgumentException("Photo cannot be blank");
        }
        if (sku.isBlank()) {
            throw new IllegalArgumentException("SKU cannot be blank");
        }

        if (price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }
    }
}