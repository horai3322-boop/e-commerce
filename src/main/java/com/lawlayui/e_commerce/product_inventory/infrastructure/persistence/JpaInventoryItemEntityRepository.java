package com.lawlayui.e_commerce.product_inventory.infrastructure.persistence;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaInventoryItemEntityRepository extends JpaRepository<InventoryItemJpaEntity, String>{
    public Optional<InventoryItemJpaEntity> findBySku(String sku);
}
