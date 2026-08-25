package com.lawlayui.e_commerce.product_catalog.application.port.out;

public interface EventPublisher {
    void publish(Object event);
}
