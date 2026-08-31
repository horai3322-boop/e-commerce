package com.lawlayui.e_commerce.inventory_test.domain;

import com.lawlayui.e_commerce.product_inventory.domain.value_object.InventoryItemId;
import com.lawlayui.e_commerce.product_inventory.domain.value_object.LocationCode;
import com.lawlayui.e_commerce.product_inventory.domain.value_object.SKU;
import com.lawlayui.e_commerce.product_inventory.domain.value_object.StockQuantitiy;
import com.lawlayui.e_commerce.product_inventory.domain.exception.InsufficientStockException;
import com.lawlayui.e_commerce.product_inventory.domain.model.InventoryItem;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * CATATAN ASUMSI:
 * Karena source code value object (InventoryItemId, SKU, LocationCode, StockQuantitiy)
 * tidak disertakan, test ini mengasumsikan constructor sederhana sebagai berikut.
 * Sesuaikan bagian factory method di bawah (createId, createSku, dst.) jika signature
 * constructor value object Anda berbeda.
 *
 *   new InventoryItemId("INV-001")
 *   new SKU("SKU-001")
 *   new LocationCode("WH-01")
 *   new StockQuantitiy(int)  -> memiliki method value()
 */
@DisplayName("InventoryItem")
class InventoryItemTest {

    private InventoryItemId id;
    private SKU sku;
    private LocationCode locationCode;
    private StockQuantitiy initialStock;

    @BeforeEach
    void setUp() {
        id = createId("INV-001");
        sku = createSku("SKU-001");
        locationCode = createLocationCode("WH1-A-03-02-B");
        initialStock = new StockQuantitiy(100);
    }

    // ---------------------------------------------------------------
    // create()
    // ---------------------------------------------------------------
    @Nested
    @DisplayName("create()")
    class Create {

        @Test
        @DisplayName("berhasil membuat InventoryItem dengan data valid")
        void shouldCreateInventoryItemWithValidData() {
            InventoryItem item = InventoryItem.create(id, sku, initialStock, locationCode);

            assertEquals(id, item.getId());
            assertEquals(sku, item.getSku());
            assertEquals(initialStock.value(), item.getAvaliableStock().value());
            assertEquals(locationCode, item.getLocationCode());
            assertEquals(0, item.getReservedStock().value());
        }

        @Test
        @DisplayName("melempar IllegalArgumentException jika id null")
        void shouldThrowWhenIdIsNull() {
            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                    () -> InventoryItem.create(null, sku, initialStock, locationCode));
            assertEquals("InventoryItemId cannot be null", ex.getMessage());
        }

        @Test
        @DisplayName("melempar IllegalArgumentException jika sku null")
        void shouldThrowWhenSkuIsNull() {
            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                    () -> InventoryItem.create(id, null, initialStock, locationCode));
            assertEquals("SKU cannot be null", ex.getMessage());
        }

        @Test
        @DisplayName("melempar IllegalArgumentException jika availableStock null")
        void shouldThrowWhenAvailableStockIsNull() {
            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                    () -> InventoryItem.create(id, sku, null, locationCode));
            assertEquals("StockQuantity cannot be null", ex.getMessage());
        }

        @Test
        @DisplayName("melempar IllegalArgumentException jika locationCode null")
        void shouldThrowWhenLocationCodeIsNull() {
            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                    () -> InventoryItem.create(id, sku, initialStock, null));
            assertEquals("LocationCode cannot be null", ex.getMessage());
        }
    }

    // ---------------------------------------------------------------
    // relocate()
    // ---------------------------------------------------------------
    @Nested
    @DisplayName("relocate()")
    class Relocate {

        @Test
        @DisplayName("berhasil mengubah lokasi ke lokasi baru")
        void shouldRelocateToNewLocation() {
            InventoryItem item = InventoryItem.create(id, sku, initialStock, locationCode);
            LocationCode newLocation = createLocationCode("WH1-A-03-02-B");

            item.relocate(newLocation);

            assertEquals(newLocation, item.getLocationCode());
        }

        @Test
        @DisplayName("melempar IllegalArgumentException jika lokasi baru null")
        void shouldThrowWhenNewLocationIsNull() {
            InventoryItem item = InventoryItem.create(id, sku, initialStock, locationCode);

            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                    () -> item.relocate(null));
            assertEquals("New LocationCode cannot be null", ex.getMessage());
        }
    }

    // ---------------------------------------------------------------
    // reserveStock()
    // ---------------------------------------------------------------
    @Nested
    @DisplayName("reserveStock()")
    class ReserveStock {

        @Test
        @DisplayName("berhasil memindahkan stok dari available ke reserved")
        void shouldMoveStockFromAvailableToReserved() {
            InventoryItem item = InventoryItem.create(id, sku, initialStock, locationCode);

            item.reserveStock(new StockQuantitiy(30));

            assertEquals(70, item.getAvaliableStock().value());
            assertEquals(30, item.getReservedStock().value());
        }

        @Test
        @DisplayName("melempar InsufficientStockException jika available stock tidak cukup")
        void shouldThrowWhenAvailableStockInsufficient() {
            InventoryItem item = InventoryItem.create(id, sku, new StockQuantitiy(10), locationCode);

            assertThrows(InsufficientStockException.class,
                    () -> item.reserveStock(new StockQuantitiy(11)));
            // pastikan state tidak berubah setelah exception
            assertEquals(10, item.getAvaliableStock().value());
            assertEquals(0, item.getReservedStock().value());
        }

        @Test
        @DisplayName("melempar IllegalArgumentException jika quantity null")
        void shouldThrowWhenQuantityIsNull() {
            InventoryItem item = InventoryItem.create(id, sku, initialStock, locationCode);

            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                    () -> item.reserveStock(null));
            assertEquals("Quantity to reserve cannot be null", ex.getMessage());
        }
    }

    // ---------------------------------------------------------------
    // deductStock()
    // ---------------------------------------------------------------
    @Nested
    @DisplayName("deductStock()")
    class DeductStock {

        @Test
        @DisplayName("berhasil mengurangi reserved stock")
        void shouldDeductFromReservedStock() {
            InventoryItem item = InventoryItem.create(id, sku, initialStock, locationCode);
            item.reserveStock(new StockQuantitiy(50));

            item.deductStock(new StockQuantitiy(20));

            assertEquals(30, item.getReservedStock().value());
            // availableStock tidak berubah oleh deductStock
            assertEquals(50, item.getAvaliableStock().value());
        }

        @Test
        @DisplayName("melempar InsufficientStockException jika reserved stock tidak cukup")
        void shouldThrowWhenReservedStockInsufficient() {
            InventoryItem item = InventoryItem.create(id, sku, initialStock, locationCode);
            item.reserveStock(new StockQuantitiy(10));

            assertThrows(InsufficientStockException.class,
                    () -> item.deductStock(new StockQuantitiy(11)));
            assertEquals(10, item.getReservedStock().value());
        }

        @Test
        @DisplayName("melempar IllegalArgumentException jika quantity null")
        void shouldThrowWhenQuantityIsNull() {
            InventoryItem item = InventoryItem.create(id, sku, initialStock, locationCode);

            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                    () -> item.deductStock(null));
            assertEquals("Quantity to deduct cannot be null", ex.getMessage());
        }
    }

    // ---------------------------------------------------------------
    // replenishStock()
    // ---------------------------------------------------------------
    @Nested
    @DisplayName("replenishStock()")
    class ReplenishStock {

        @Test
        @DisplayName("berhasil menambah available stock")
        void shouldIncreaseAvailableStock() {
            InventoryItem item = InventoryItem.create(id, sku, initialStock, locationCode);

            item.replenishStock(new StockQuantitiy(25));

            assertEquals(125, item.getAvaliableStock().value());
        }

        @Test
        @DisplayName("melempar IllegalArgumentException jika quantity null")
        void shouldThrowWhenQuantityIsNull() {
            InventoryItem item = InventoryItem.create(id, sku, initialStock, locationCode);

            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                    () -> item.replenishStock(null));
            assertEquals("Quantity to replenish cannot be null", ex.getMessage());
        }
    }

    // ---------------------------------------------------------------
    // releaseReservedStock()
    // ---------------------------------------------------------------
    @Nested
    @DisplayName("releaseReservedStock()")
    class ReleaseReservedStock {

        @Test
        @DisplayName("berhasil memindahkan stok dari reserved kembali ke available")
        void shouldMoveStockFromReservedToAvailable() {
            InventoryItem item = InventoryItem.create(id, sku, initialStock, locationCode);
            item.reserveStock(new StockQuantitiy(40));

            item.releaseReservedStock(new StockQuantitiy(15));

            assertEquals(25, item.getReservedStock().value());
            assertEquals(75, item.getAvaliableStock().value());
        }

        @Test
        @DisplayName("melempar InsufficientStockException jika reserved stock tidak cukup untuk dilepas")
        void shouldThrowWhenReservedStockInsufficient() {
            InventoryItem item = InventoryItem.create(id, sku, initialStock, locationCode);
            item.reserveStock(new StockQuantitiy(5));

            assertThrows(InsufficientStockException.class,
                    () -> item.releaseReservedStock(new StockQuantitiy(6)));
            assertEquals(5, item.getReservedStock().value());
            assertEquals(95, item.getAvaliableStock().value());
        }

        @Test
        @DisplayName("melempar IllegalArgumentException jika quantity null")
        void shouldThrowWhenQuantityIsNull() {
            InventoryItem item = InventoryItem.create(id, sku, initialStock, locationCode);

            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                    () -> item.releaseReservedStock(null));
            assertEquals("Quantity to release cannot be null", ex.getMessage());
        }
    }

    // ---------------------------------------------------------------
    // Skenario alur gabungan (integration-ish, tetap dalam satu unit)
    // ---------------------------------------------------------------
    @Test
    @DisplayName("alur reserve -> deduct -> replenish konsisten menjaga total stok")
    void shouldKeepStockConsistentAcrossFullFlow() {
        InventoryItem item = InventoryItem.create(id, sku, new StockQuantitiy(50), locationCode);

        item.reserveStock(new StockQuantitiy(20));   // available 30, reserved 20
        item.deductStock(new StockQuantitiy(20));    // available 30, reserved 0 (terjual habis)
        item.replenishStock(new StockQuantitiy(10)); // available 40, reserved 0

        assertEquals(40, item.getAvaliableStock().value());
        assertEquals(0, item.getReservedStock().value());
    }

    // ---------------------------------------------------------------
    // Helper factory - SESUAIKAN dengan constructor asli value object Anda
    // ---------------------------------------------------------------
    private InventoryItemId createId(String value) {
        return new InventoryItemId(value);
    }

    private SKU createSku(String value) {
        return new SKU(value);
    }

    private LocationCode createLocationCode(String value) {
        return new LocationCode(value);
    }
}