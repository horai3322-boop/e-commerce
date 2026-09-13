package com.lawlayui.e_commerce.product_inventory.infrastructure.web;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.lawlayui.e_commerce.product_inventory.application.exception.InventoryItemNotFound;
import com.lawlayui.e_commerce.product_inventory.application.port.in.ErrorDto;


@ControllerAdvice 
public class InventoryGlobalExceptionHandler {
    @ExceptionHandler(IllegalArgumentException.class)
    public ErrorDto handleIllegalArgumentException(IllegalArgumentException ex) {
        return new ErrorDto("INVALID_ARGUMENT", ex.getMessage());
    }

    @ExceptionHandler(NullPointerException.class)
    public ErrorDto handleNullPointerException(NullPointerException ex) {
        return new ErrorDto("NULL_POINTER", ex.getMessage());
    }

    @ExceptionHandler(InventoryItemNotFound.class)
    public ErrorDto handleInventoryItemNotFound(InventoryItemNotFound ex) {
        return new ErrorDto("INVENTORY_ITEM_NOT_FOUND", ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ErrorDto handleGenericException(Exception ex) {
        return new ErrorDto("INTERNAL_SERVER_ERROR", ex.getMessage());
    }
}
