package com.lawlayui.e_commerce.product_catalog.infrastructure.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.lawlayui.e_commerce.product_catalog.application.port.in.ErrorDto;

@ControllerAdvice
public class ProductCatalogGlobalExceptionHandler {
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorDto> handleIllegalArgumentException(IllegalArgumentException ex) {
        ErrorDto errorDto = new ErrorDto("INVALID_ARGUMENT", ex.getMessage());
        return ResponseEntity.badRequest().body(errorDto);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ErrorDto> handleNullPointerException(NullPointerException ex) {
        ErrorDto errorDto = new ErrorDto("NULL_POINTER", ex.getMessage());
        return ResponseEntity.status(500).body(errorDto);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> handleGenericException(Exception ex) {
        ErrorDto errorDto = new ErrorDto("INTERNAL_SERVER_ERROR", ex.getMessage());
        return ResponseEntity.status(500).body(errorDto);
    }
}
