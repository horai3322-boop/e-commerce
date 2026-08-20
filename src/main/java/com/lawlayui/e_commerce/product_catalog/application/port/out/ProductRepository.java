package com.lawlayui.e_commerce.product_catalog.application.port.out;

import java.util.List;

import com.lawlayui.e_commerce.product_catalog.application.port.in.GetAllProductQuery;
import com.lawlayui.e_commerce.product_catalog.application.port.in.GetProductByIdQuery;
import com.lawlayui.e_commerce.product_catalog.application.port.in.ProductDto;
import com.lawlayui.e_commerce.product_catalog.domain.model.Product;

public interface ProductRepository {
    public ProductDto getById(GetProductByIdQuery query);
    public List<ProductDto> getAll(GetAllProductQuery query);
    public ProductDto save(Product product);
    public void delete(Product product);
}
