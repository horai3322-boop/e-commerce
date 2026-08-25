package com.lawlayui.e_commerce.product_catalog.application.port.in;

import java.math.BigDecimal;

public record EditCatalogInformationCommand(String productId, String name, String description, String filePath, BigDecimal price) {
    public EditCatalogInformationCommand {
        if (productId == null || productId.isBlank()) {
            throw new IllegalArgumentException("Product ID cannot be null or blank");
        }
    }
}
