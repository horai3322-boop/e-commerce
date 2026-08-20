package com.lawlayui.e_commerce.product_catalog.domain.exception;

public class ProductValueBlankOrEmtpy extends RuntimeException{
    public ProductValueBlankOrEmtpy(String valueObject) {
        super(valueObject + " cannot be blank or empty");
    }
}
