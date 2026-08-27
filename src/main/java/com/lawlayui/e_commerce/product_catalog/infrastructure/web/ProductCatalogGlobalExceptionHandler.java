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
}
