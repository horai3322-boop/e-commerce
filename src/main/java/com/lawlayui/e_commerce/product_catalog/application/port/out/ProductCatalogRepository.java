package com.lawlayui.e_commerce.product_catalog.application.port.out;

import java.util.List;
import java.util.Optional;

import com.lawlayui.e_commerce.product_catalog.domain.model.Product;
import com.lawlayui.e_commerce.product_catalog.domain.value_object.ProductId;

public interface ProductCatalogRepository {
    public Optional<Product> getById(ProductId productId);
    public List<Product> getAll(int page, int pageSize, String searchKey);
    public Product save(Product product);
    public void delete(Product product);
}
