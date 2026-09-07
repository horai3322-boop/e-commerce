package com.lawlayui.e_commerce.inventory_test.application;

import com.lawlayui.e_commerce.product_inventory.application.exception.InventoryItemNotFound;
import com.lawlayui.e_commerce.product_inventory.application.port.in.ReserveStockCommand;
import com.lawlayui.e_commerce.product_inventory.application.port.out.InventoryRepository;
import com.lawlayui.e_commerce.product_inventory.application.service.ReserveStockImpl;
import com.lawlayui.e_commerce.product_inventory.domain.model.InventoryItem;
import com.lawlayui.e_commerce.product_inventory.domain.value_object.InventoryItemId;
import com.lawlayui.e_commerce.product_inventory.domain.value_object.LocationCode;
import com.lawlayui.e_commerce.product_inventory.domain.value_object.SKU;
import com.lawlayui.e_commerce.product_inventory.domain.value_object.StockQuantitiy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ReserveStockImpl Use Case Test")
class ReserveStockImplTest {

    @Mock
    private InventoryRepository inventoryRepository;

    private ReserveStockImpl reserveStockImpl;

    @BeforeEach
    void setUp() {
        reserveStockImpl = new ReserveStockImpl(inventoryRepository);
    }

    private InventoryItem buildInventoryItem(String sku, int availableStock, String locationCode) {
        return InventoryItem.create(
                InventoryItemId.generateId(),
                new SKU(sku),
                new StockQuantitiy(availableStock),
                new LocationCode(locationCode)
        );
    }

    @Test
    @DisplayName("Should call findBySKU exactly once with the correct SKU")
    void shouldCallFindBySkuOnce() {
        // Arrange
        ReserveStockCommand command = new ReserveStockCommand("SKU-001", 5);
        InventoryItem inventoryItem = spy(buildInventoryItem("SKU-001", 10, "CGK01-PICK-12-04-01"));
        when(inventoryRepository.findBySKU("SKU-001")).thenReturn(Optional.of(inventoryItem));

        // Act
        reserveStockImpl.execute(command);

        // Assert
        verify(inventoryRepository, times(1)).findBySKU("SKU-001");
    }

    @Test
    @DisplayName("Should throw InventoryItemNotFound when inventory item does not exist")
    void shouldThrowInventoryItemNotFoundWhenItemDoesNotExist() {
        // Arrange
        ReserveStockCommand command = new ReserveStockCommand("SKU-404", 5);
        when(inventoryRepository.findBySKU("SKU-404")).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> reserveStockImpl.execute(command))
                .isInstanceOf(InventoryItemNotFound.class);

        verify(inventoryRepository, times(1)).findBySKU("SKU-404");
        verify(inventoryRepository, never()).save(any(InventoryItem.class));
    }

    @Test
    @DisplayName("Should call reserveStock on the found inventory item with correct quantity")
    void shouldCallReserveStockOnFoundItem() {
        // Arrange
        ReserveStockCommand command = new ReserveStockCommand("SKU-001", 5);
        InventoryItem inventoryItem = spy(buildInventoryItem("SKU-001", 10, "CGK01-PICK-12-04-01"));
        when(inventoryRepository.findBySKU("SKU-001")).thenReturn(Optional.of(inventoryItem));

        // Act
        reserveStockImpl.execute(command);

        // Assert
        verify(inventoryItem, times(1)).reserveStock(any(StockQuantitiy.class));
    }

    @Test
    @DisplayName("Should call save exactly once after successfully reserving stock")
    void shouldCallSaveOnce() {
        // Arrange
        ReserveStockCommand command = new ReserveStockCommand("SKU-001", 5);
        InventoryItem inventoryItem = spy(buildInventoryItem("SKU-001", 10, "CGK01-PICK-12-04-01"));
        when(inventoryRepository.findBySKU("SKU-001")).thenReturn(Optional.of(inventoryItem));

        // Act
        reserveStockImpl.execute(command);

        // Assert
        verify(inventoryRepository, times(1)).save(inventoryItem);
    }

    @Test
    @DisplayName("Should execute operations in correct order: find, reserve, then save")
    void shouldExecuteOperationsInCorrectOrder() {
        // Arrange
        ReserveStockCommand command = new ReserveStockCommand("SKU-001", 5);
        InventoryItem inventoryItem = spy(buildInventoryItem("SKU-001", 10, "CGK01-PICK-12-04-01"));
        when(inventoryRepository.findBySKU("SKU-001")).thenReturn(Optional.of(inventoryItem));

        // Act
        reserveStockImpl.execute(command);

        // Assert
        var inOrder = inOrder(inventoryRepository, inventoryItem);
        inOrder.verify(inventoryRepository).findBySKU("SKU-001");
        inOrder.verify(inventoryItem).reserveStock(any(StockQuantitiy.class));
        inOrder.verify(inventoryRepository).save(inventoryItem);
    }
}