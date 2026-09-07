package com.lawlayui.e_commerce.product_inventory.infrastructure.persistence;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.lawlayui.e_commerce.product_inventory.domain.model.InventoryItem;
import com.lawlayui.e_commerce.product_inventory.domain.value_object.InventoryItemId;
import com.lawlayui.e_commerce.product_inventory.domain.value_object.LocationCode;
import com.lawlayui.e_commerce.product_inventory.domain.value_object.SKU;
import com.lawlayui.e_commerce.product_inventory.domain.value_object.StockQuantitiy;

@Mapper(componentModel = "spring")
public interface InventoryItemPersistentMapper {
    @Mapping(source = "id", target = "id", qualifiedByName = "mapInventoryItemIdToString")
    @Mapping(source = "sku", target = "sku", qualifiedByName = "mapSKUtoString")
    @Mapping(source = "availableStock", target = "availableStock", qualifiedByName = "mapStockQuantityToInteger")
    @Mapping(source = "reservedStock", target = "reservedStock", qualifiedByName = "mapStockQuantityToInteger")
    @Mapping(source = "locationCode", target = "locationCode", qualifiedByName = "mapLocationCodeToString")
    InventoryItemJpaEntity toEntity(InventoryItem inventoryItem);
    InventoryItem toDomain(InventoryItemJpaEntity entity);

    @Named("mapInventoryItemIdToString")
    default String mapInventoryItemIdToString(InventoryItemId inventoryItemId) {
        return inventoryItemId != null ? inventoryItemId.value() : null;
    }

    @Named("mapSKUtoString")
    default String mapSKUtoString(SKU sku) {
        return sku != null ? sku.value() : null;
    }

    @Named("mapLocationCodeToString")
    default String mapLocationCodeToString(LocationCode locationCode) {
        return locationCode != null ? locationCode.value() : null;
    }

    @Named("mapStockQuantityToInteger")
    default Integer mapStockQuantityToInteger(StockQuantitiy stockQuantity) {
        return stockQuantity != null ? stockQuantity.value() : null;
    }
}
