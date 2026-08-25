package com.lawlayui.e_commerce.product_catalog.application.port.in;

import java.math.BigDecimal;

import com.lawlayui.e_commerce.product_catalog.application.exception.ProductDataEmptyException;
import com.lawlayui.e_commerce.product_catalog.application.exception.ProductLengthException;
import com.lawlayui.e_commerce.product_catalog.application.exception.ProductPriceZeroException;

public record AddProductCommand(String name, String descirption, BigDecimal price, String file_path, int initialStock) {
    public AddProductCommand {
        if (name.length() < 3) {
            throw new ProductLengthException(255, 3, "name");
        }
        if (name.isEmpty()) {
            throw new ProductDataEmptyException("name");
        }
        if (descirption.length() < 50) {
            throw new ProductLengthException(255, 50, "description");
        }
        if (descirption.isEmpty()) {
            throw new ProductDataEmptyException("description");
        }
        if (price.compareTo(BigDecimal.ZERO) == 0) {
            throw new ProductPriceZeroException();
        }
        if (file_path.isEmpty()) {
            throw new ProductDataEmptyException("file_path");
        }
        if (initialStock < 0) {
            throw new ProductDataEmptyException("initialStock");
        }
    }   
}
