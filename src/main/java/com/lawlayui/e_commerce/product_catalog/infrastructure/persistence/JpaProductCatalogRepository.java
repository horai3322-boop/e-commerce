package com.lawlayui.e_commerce.product_catalog.infrastructure.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import com.lawlayui.e_commerce.product_catalog.application.port.out.ProductCatalogRepository;
import com.lawlayui.e_commerce.product_catalog.domain.model.Product;

@Repository
public class JpaProductCatalogRepository implements ProductCatalogRepository {
    private final JpaProductCatalogEntityRepository jpaRepository;
    private final ProductPersistentMapper productMapping;

    public JpaProductCatalogRepository(JpaProductCatalogEntityRepository jpaRepository, ProductPersistentMapper productMapping) {
        this.jpaRepository = jpaRepository;
        this.productMapping = productMapping;
    }

    @Override
    public List<Product> getAll(int page, int pageSize, String searchKey) {
        List<ProductCatalogJpaEntity> entities = jpaRepository.searchByKeyword(searchKey, PageRequest.of(page, pageSize)).getContent();
        return productMapping.toDomainList(entities);
    }

    @Override
    public Optional<Product> getBySku(String sku) {
        Optional<ProductCatalogJpaEntity> entity = jpaRepository.findBySku(sku);
        return entity.map(productMapping::toDomain);
    }

    @Override
    public Product save(Product product) {
        ProductCatalogJpaEntity entityToSave = jpaRepository.findBySku(product.getProductSku().value())
            .map(existingEntity -> {
                existingEntity.setProductName(product.getProductName().productName());
                existingEntity.setProductDescription(product.getProductDescription().desc());
                existingEntity.setProductPhoto(product.getProductPhoto().filePath() != null ? product.getProductPhoto().filePath() : null);
                existingEntity.setProductPrice(product.getProductPrice().price());
                existingEntity.setStatus(product.getStatus().name());
                return existingEntity;
        })
            .orElseGet(() -> productMapping.toEntity(product)); 

        ProductCatalogJpaEntity savedEntity = jpaRepository.save(entityToSave);

        return productMapping.toDomain(savedEntity);
    }

    @Override 
    public boolean existsBySKu(String sku) {
        return jpaRepository.existsBySku(sku);
    }
}
