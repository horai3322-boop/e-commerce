package com.lawlayui.e_commerce.product_catalog.application.port.in;

import java.math.BigDecimal;

public record EditCatalogInformationCommand(String sku, String name, String description, String photo, BigDecimal price) {
    public EditCatalogInformationCommand {
        if (name.length() < 3 || name.length() > 255) {
            throw new IllegalArgumentException("The character name must be between 3 and 255 characters long");
        }

        if (description.length() < 50 || description.length() > 1000) {
            throw new IllegalArgumentException("The character description must be between 50 and 1000 characters long");
        }

        if (price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }
    }
}
