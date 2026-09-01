package com.lawlayui.e_commerce.product_inventory.application.port.in;

public interface ReleaseStockUseCase {
    void execute(ReleaseStockCommand command);
}
