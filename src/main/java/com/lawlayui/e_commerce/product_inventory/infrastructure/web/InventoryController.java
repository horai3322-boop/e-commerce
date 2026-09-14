package com.lawlayui.e_commerce.product_inventory.infrastructure.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lawlayui.e_commerce.product_inventory.application.port.in.GetInventoryItemQuery;
import com.lawlayui.e_commerce.product_inventory.application.port.in.GetInventoryItemUseCase;
import com.lawlayui.e_commerce.product_inventory.application.port.in.InventoryItemDto;
import com.lawlayui.e_commerce.product_inventory.application.port.in.ReleaseStockUseCase;
import com.lawlayui.e_commerce.product_inventory.application.port.in.RelocateStockCommand;
import com.lawlayui.e_commerce.product_inventory.application.port.in.RelocateStockUseCase;
import com.lawlayui.e_commerce.product_inventory.application.port.in.ReplenishStockCommand;
import com.lawlayui.e_commerce.product_inventory.application.port.in.ReplenishStockUseCase;

@RestController
@RequestMapping("/api/v1/products/inventory/{sku}")
public class InventoryController {
    private final RelocateStockUseCase  relocateStockUseCase;
    private final ReplenishStockUseCase replenishStockUseCase;
    private final GetInventoryItemUseCase getInventoryItemUseCase;

    public InventoryController(ReleaseStockUseCase releaseStockUseCase, RelocateStockUseCase relocateStockUseCase, ReplenishStockUseCase replenishStockUseCase, GetInventoryItemUseCase getInventoryItemUseCase) {
        this.relocateStockUseCase = relocateStockUseCase;
        this.replenishStockUseCase = replenishStockUseCase;
        this.getInventoryItemUseCase = getInventoryItemUseCase;
    } 
        

    @GetMapping
    public ResponseEntity<InventoryItemDto> getInventoryItem(@PathVariable String sku) {
        InventoryItemDto inventoryItemDto = getInventoryItemUseCase.execute(new GetInventoryItemQuery(sku));
        return ResponseEntity.ok(inventoryItemDto);
    }

    @PatchMapping("/relocate-stock")
    public ResponseEntity<?> relocateStock(@PathVariable String sku, @RequestBody RelocateStockCommand command) {
        relocateStockUseCase.execute(new RelocateStockCommand(sku, command.newLocationCode()));
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/replenish-stock")
    public ResponseEntity<?> replenishStock(@PathVariable String sku, @RequestBody ReplenishStockCommand command) {
        replenishStockUseCase.execute(new ReplenishStockCommand(sku, command.quantity()));
        return ResponseEntity.noContent().build();
    }
}
