package com.lawlayui.e_commerce.product_catalog.infrastructure.persistence;

import java.util.List;
import java.util.Optional;

import com.lawlayui.e_commerce.product_catalog.application.port.out.ProductCatalogRepository;
import com.lawlayui.e_commerce.product_catalog.domain.model.Product;
import com.lawlayui.e_commerce.product_catalog.domain.value_object.ProductId;

public class JpaProductCatalogRepository implements ProductCatalogRepository {
    private final JpaProductCatalogEntityRepository jpaRepository;

    public JpaProductCatalogRepository(JpaProductCatalogEntityRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public void delete(Product product) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public List<Product> getAll(int page, int pageSize, String searchKey) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Optional<Product> getById(ProductId productId) {
        // TODO Auto-generated method stub
        return Optional.empty();
    }

    @Override
    public Product save(Product product) {
        // TODO Auto-generated method stub
        return null;
    }

}
