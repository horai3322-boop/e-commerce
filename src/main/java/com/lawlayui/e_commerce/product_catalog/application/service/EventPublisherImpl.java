package com.lawlayui.e_commerce.product_catalog.application.service;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import com.lawlayui.e_commerce.product_catalog.application.port.out.EventPublisher;

@Service
public class EventPublisherImpl implements EventPublisher {
    private final ApplicationEventPublisher eventPublisher;

    public EventPublisherImpl(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }
    @Override
    public void publish(Object event) {
        eventPublisher.publishEvent(event);
    }
    
}
