package com.lawlayui.e_commerce.product_inventory.application.port.out;

public interface EventPublisher {
    void publish(Object event);
}
