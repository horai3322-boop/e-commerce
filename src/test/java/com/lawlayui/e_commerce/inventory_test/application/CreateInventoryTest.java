package com.lawlayui.e_commerce.inventory_test.application;

import com.lawlayui.e_commerce.product_catalog.application.port.out.EventPublisher;
import com.lawlayui.e_commerce.product_inventory.application.service.CreateInventoryImpl;
import com.lawlayui.e_commerce.product_inventory.application.event.InventoryCreatedEvent;
import com.lawlayui.e_commerce.product_inventory.application.port.in.CreateInventoryCommand;
import com.lawlayui.e_commerce.product_inventory.application.port.out.InventoryRepository;
import com.lawlayui.e_commerce.product_inventory.domain.model.InventoryItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
@DisplayName("CreateInventoryImpl Use Case Test")
class CreateInventoryImplTest {

    @Mock
    private InventoryRepository inventoryRepository;

    @Mock
    private EventPublisher eventPublisher;

    private CreateInventoryImpl createInventoryImpl;

    @BeforeEach
    void setUp() {
        createInventoryImpl = new CreateInventoryImpl(inventoryRepository, eventPublisher);
    }

    @Test
    @DisplayName("Should save inventory item and publish event exactly once when command is valid")
    void shouldSaveInventoryAndPublishEventOnce() {
        // Arrange
        CreateInventoryCommand command = new CreateInventoryCommand("SKU-001", 10, "WH1-A-03-02-B");

        // Act
        createInventoryImpl.execute(command);

        // Assert
        verify(inventoryRepository, times(1)).save(any(InventoryItem.class));
        verify(eventPublisher, times(1)).publish(any(InventoryCreatedEvent.class));
    }

    @Test
    @DisplayName("Should publish event with correct data derived from the saved inventory item")
    void shouldPublishEventWithCorrectData() {
        // Arrange
        CreateInventoryCommand command = new CreateInventoryCommand("SKU-002", 25, "WH1-A-03-02-B");
        ArgumentCaptor<InventoryCreatedEvent> eventCaptor = ArgumentCaptor.forClass(InventoryCreatedEvent.class);
        ArgumentCaptor<InventoryItem> itemCaptor = ArgumentCaptor.forClass(InventoryItem.class);

        // Act
        createInventoryImpl.execute(command);

        // Assert
        verify(inventoryRepository).save(itemCaptor.capture());
        verify(eventPublisher).publish(eventCaptor.capture());

        InventoryItem savedItem = itemCaptor.getValue();
        InventoryCreatedEvent publishedEvent = eventCaptor.getValue();

        assertThat(publishedEvent.sku()).isEqualTo(savedItem.getSku().value());
        assertThat(publishedEvent.quantity()).isEqualTo(25);
        assertThat(publishedEvent.locationCode()).isEqualTo("WH1-A-03-02-B");
        assertThat(publishedEvent.id()).isEqualTo(savedItem.getId().value());
    }

    @Test
    @DisplayName("Should not call repository or publisher when command construction fails")
    void shouldNotInteractWithDependenciesWhenCommandIsInvalid() {
        // Assert construction itself fails before use case is even invoked
        assertThatThrownBy(() -> new CreateInventoryCommand(null, 10, "WH-JKT-01"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("SKU cannot be null or empty");

        // No interaction should have happened since command was never created
        verifyNoInteractions(inventoryRepository, eventPublisher);
    }
}