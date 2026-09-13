package com.lawlayui.e_commerce.product_catalog.application.port.out;

import java.util.List;
import java.util.Optional;

import com.lawlayui.e_commerce.product_catalog.domain.model.Product;

public interface ProductCatalogRepository {
    public List<Product> getAll(int page, int pageSize, String searchKey);
    public Optional<Product> getBySku(String sku);
    public Product save(Product product);
    boolean existsBySKu(String sku);
}
