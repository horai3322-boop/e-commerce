package com.lawlayui.e_commerce.product_inventory.infrastructure.persistence;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.lawlayui.e_commerce.product_inventory.application.port.out.InventoryRepository;
import com.lawlayui.e_commerce.product_inventory.domain.model.InventoryItem;

@Repository 
public class InventoryItemRepository implements InventoryRepository{

    private final JpaInventoryItemEntityRepository jpaRepository;
    private final InventoryItemPersistentMapper mapper;

    public InventoryItemRepository(JpaInventoryItemEntityRepository jpaRepository, InventoryItemPersistentMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Optional<InventoryItem> findBySKU(String sku) {
        Optional<InventoryItemJpaEntity> entityOptional = jpaRepository.findBySku(sku);
        return entityOptional.map(entity -> mapper.toDomain(entity));
    }

    @Override
    public void save(InventoryItem inventoryItem) {
        InventoryItemJpaEntity entity = mapper.toEntity(inventoryItem);
        jpaRepository.save(entity);
    }
    
}
