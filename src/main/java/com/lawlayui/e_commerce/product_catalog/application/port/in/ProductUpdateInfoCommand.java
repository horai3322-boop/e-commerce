package com.lawlayui.e_commerce.product_catalog.application.port.in;

import java.math.BigDecimal;

public record ProductUpdateInfoCommand(Long id, String name, String descirption, BigDecimal price, String filePath) {

}
