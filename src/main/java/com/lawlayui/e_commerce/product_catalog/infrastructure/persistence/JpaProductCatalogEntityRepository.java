package com.lawlayui.e_commerce.product_catalog.infrastructure.persistence;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface JpaProductCatalogEntityRepository extends JpaRepository<ProductCatalogJpaEntity, String> {
    @Query("SELECT p FROM ProductCatalogJpaEntity p WHERE " +
       "LOWER(p.productName) LIKE LOWER(:keyword) OR " +
       "LOWER(p.productDescription) LIKE LOWER(:keyword) OR " +
       "LOWER(p.status) LIKE LOWER(:keyword)")
    Page<ProductCatalogJpaEntity> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);

    boolean existsBySku(String sku);

    Optional<ProductCatalogJpaEntity> findBySku(String sku);
}
