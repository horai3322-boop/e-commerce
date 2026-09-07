package com.lawlayui.e_commerce.inventory_test.application;

import com.lawlayui.e_commerce.product_inventory.application.port.in.CreateInventoryCommand;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("CreateInventoryCommand Validation Test")
class CreateInventoryCommandTest {

    @Test
    @DisplayName("Should throw IllegalArgumentException when SKU is null")
    void shouldThrowWhenSkuIsNull() {
        assertThatThrownBy(() -> new CreateInventoryCommand(null, 10, "WH-JKT-01"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("SKU cannot be null or empty");
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when SKU is empty")
    void shouldThrowWhenSkuIsEmpty() {
        assertThatThrownBy(() -> new CreateInventoryCommand("", 10, "WH-JKT-01"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("SKU cannot be null or empty");
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when quantity is negative")
    void shouldThrowWhenQuantityIsNegative() {
        assertThatThrownBy(() -> new CreateInventoryCommand("SKU-001", -1, "WH-JKT-01"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Quantity cannot be negative");
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when location code is null")
    void shouldThrowWhenLocationCodeIsNull() {
        assertThatThrownBy(() -> new CreateInventoryCommand("SKU-001", 10, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Location code cannot be null or empty");
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when location code is empty")
    void shouldThrowWhenLocationCodeIsEmpty() {
        assertThatThrownBy(() -> new CreateInventoryCommand("SKU-001", 10, ""))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Location code cannot be null or empty");
    }

    @Test
    @DisplayName("Should construct successfully when all fields are valid")
    void shouldConstructSuccessfullyWithValidData() {
        assertThatCode(() -> new CreateInventoryCommand("SKU-001", 10, "WH-JKT-01"))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Should allow zero as a valid quantity")
    void shouldAllowZeroQuantity() {
        assertThatCode(() -> new CreateInventoryCommand("SKU-001", 0, "WH-JKT-01"))
                .doesNotThrowAnyException();
    }
}