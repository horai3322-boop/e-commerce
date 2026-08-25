package com.lawlayui.e_commerce.product_catalog.infrastructure.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface JpaProductCatalogEntityRepository extends JpaRepository<ProductCatalogJpaEntity, String> {
    @Query("SELECT p FROM ProductCatalogJpaEntity p WHERE " +
       "LOWER(p.productName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
       "LOWER(p.productDescription) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
       "LOWER(p.status) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<ProductCatalogJpaEntity> searchByKeyword(@Param("keyword") String keyword);
}
