package com.lawlayui.e_commerce.product_catalog.infrastructure.web;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.lawlayui.e_commerce.product_catalog.application.exception.CatalogNotFoundException;
import com.lawlayui.e_commerce.product_catalog.application.port.in.ErrorDto;

@ControllerAdvice
public class ProductCatalogGlobalExceptionHandler {
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
        return ResponseEntity.badRequest().body(errorDto);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ErrorDto> handleNullPointerException(NullPointerException ex) {
        ErrorDto errorDto = new ErrorDto("NULL_POINTER", ex.getMessage());
        return ResponseEntity.status(400).body(errorDto);
    }

    @ExceptionHandler(CatalogNotFoundException.class) 
    public ResponseEntity<ErrorDto> handleCatalogNotFoundExcepion(CatalogNotFoundException ex) {
        ErrorDto errorDto = new ErrorDto("NOT_FOUND", ex.getMessage());
        return ResponseEntity.status(404).body(errorDto);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> handleGenericException(Exception ex) {
        ErrorDto errorDto = new ErrorDto("INTERNAL_SERVER_ERROR", ex.getMessage());
        return ResponseEntity.status(500).body(errorDto);
    }
}
