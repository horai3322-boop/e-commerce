package com.lawlayui.e_commerce.product_inventory.infrastructure.web;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.lawlayui.e_commerce.product_inventory.application.exception.InventoryItemNotFound;
import com.lawlayui.e_commerce.product_inventory.application.port.in.ErrorDto;

@RestControllerAdvice(basePackages = "com.lawlayui.e_commerce.product_inventory")
public class InventoryGlobalExceptionHandler {
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorDto> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        Throwable rootCause = ex.getRootCause();

        if (rootCause instanceof NullPointerException) {
            ErrorDto errorDto = new ErrorDto("NULL_POINTER", rootCause.getMessage());
            return ResponseEntity.status(400).body(errorDto);
        }
    
        if (rootCause instanceof IllegalArgumentException) {
            ErrorDto errorDto = new ErrorDto("INVALID_ARGUMENT", rootCause.getMessage());
            return ResponseEntity.status(400).body(errorDto);
        }

        ErrorDto errorDto = new ErrorDto("MALFORMED_JSON", "Format payload JSON invalid");
        return ResponseEntity.status(400).body(errorDto);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorDto> handleIllegalArgumentException(IllegalArgumentException ex) {
        ErrorDto errorDto = new ErrorDto("INVALID_ARGUMENT", ex.getMessage());
        return ResponseEntity.status(400).body(errorDto);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ErrorDto> handleNullPointerException(NullPointerException ex) {
        ErrorDto errorDto = new ErrorDto("NULL_POINTER", ex.getMessage());
        return ResponseEntity.status(400).body(errorDto);
    }

    @ExceptionHandler(InventoryItemNotFound.class)
    public ResponseEntity<ErrorDto> handleInventoryItemNotFound(InventoryItemNotFound ex) {
        ErrorDto errorDto = new ErrorDto("NOT_FOUND", ex.getMessage());
        return ResponseEntity.status(404).body(errorDto);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> handleGenericException(Exception ex) {
        ErrorDto errorDto = new ErrorDto("INTERNAL_SERVER", ex.getMessage());
        return ResponseEntity.status(500).body(errorDto);
    }
}
