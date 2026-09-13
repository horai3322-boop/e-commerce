package com.lawlayui.e_commerce.product_inventory.application.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.lawlayui.e_commerce.product_inventory.application.port.in.InventoryItemDto;
import com.lawlayui.e_commerce.product_inventory.domain.model.InventoryItem;
import com.lawlayui.e_commerce.product_inventory.domain.value_object.LocationCode;
import com.lawlayui.e_commerce.product_inventory.domain.value_object.SKU;

@Mapper(componentModel = "spring")
public interface InventoryItemMapping {

    @Mapping(source = "sku", target = "sku", qualifiedByName = "skuToString")
    @Mapping(source = "locationCode", target = "locationCode", qualifiedByName = "locationCodeToString")
    @Mapping(source = "availableStock.value", target = "quantity")
    InventoryItemDto toDto(InventoryItem inventoryItem);


    @Named("skuToString")
    default String skuToString(SKU sku) {
        return sku != null ? sku.value() : null;
    }

    @Named("locationCodeToString")
    default String locationCodeToString(LocationCode locationCode) {
        return locationCode != null ? locationCode.value() : null;
    }
}
