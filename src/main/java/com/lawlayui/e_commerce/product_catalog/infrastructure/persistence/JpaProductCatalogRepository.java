package com.lawlayui.e_commerce.product_catalog.infrastructure.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;


import com.lawlayui.e_commerce.product_catalog.application.port.out.ProductCatalogRepository;
import com.lawlayui.e_commerce.product_catalog.domain.model.Product;
import com.lawlayui.e_commerce.product_catalog.domain.value_object.ProductId;

public class JpaProductCatalogRepository implements ProductCatalogRepository {
    private final JpaProductCatalogEntityRepository jpaRepository;
    private final ProductMapping productMapping;

    public JpaProductCatalogRepository(JpaProductCatalogEntityRepository jpaRepository, ProductMapping productMapping) {
        this.jpaRepository = jpaRepository;
        this.productMapping = productMapping;
    }

    @Override
    public void delete(Product product) {
        jpaRepository.deleteById(product.getProductId().id().toString());
    }

    @Override
    public List<Product> getAll(int page, int pageSize, String searchKey) {
        List<ProductCatalogJpaEntity> entities = jpaRepository.searchByKeyword(searchKey, PageRequest.of(page, pageSize)).getContent();
        return productMapping.toDomainList(entities);
    }

    @Override
    public Optional<Product> getById(ProductId productId) {
        Optional<ProductCatalogJpaEntity> entity = jpaRepository.findById(productId.id().toString());
        return entity.map(productMapping::toDomain);
    }

    @Override
    public Product save(Product product) {
        ProductCatalogJpaEntity entity = productMapping.toEntity(product);
        ProductCatalogJpaEntity savedEntity = jpaRepository.save(entity);
        return productMapping.toDomain(savedEntity);
    }

}
