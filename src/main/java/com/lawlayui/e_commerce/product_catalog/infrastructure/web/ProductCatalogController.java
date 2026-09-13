package com.lawlayui.e_commerce.product_catalog.infrastructure.web;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lawlayui.e_commerce.product_catalog.application.port.in.AddProductCommand;
import com.lawlayui.e_commerce.product_catalog.application.port.in.EditCatalogInformationCommand;
import com.lawlayui.e_commerce.product_catalog.application.port.in.GetProductBySkuQuery;
import com.lawlayui.e_commerce.product_catalog.application.port.in.ProductDto;
import com.lawlayui.e_commerce.product_catalog.application.port.in.RemoveProductBySkuCommand;
import com.lawlayui.e_commerce.product_catalog.application.port.in.SearchProductQuery;
import com.lawlayui.e_commerce.product_catalog.application.service.CatalogServicesAdapter;

@RestController
@RequestMapping("/api/v1/products")
public class ProductCatalogController {

    private final CatalogServicesAdapter catalogServicesAdapter;

    public ProductCatalogController(CatalogServicesAdapter catalogServicesAdapter) {
        this.catalogServicesAdapter = catalogServicesAdapter;
    }

    @GetMapping
    public ResponseEntity<List<ProductDto>> search(@RequestParam(defaultValue = "0") int page,
                                                   @RequestParam(defaultValue = "10") int pageSize,
                                                   @RequestParam(defaultValue = "") String q) {
        List<ProductDto> products = catalogServicesAdapter.searchProductUseCase(new SearchProductQuery(page, pageSize, q));
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{sku}")
    public ResponseEntity<ProductDto> getBySku(@PathVariable String sku) {
        ProductDto productDto = catalogServicesAdapter.getProductBySkuUseCase(new GetProductBySkuQuery(sku));
        return ResponseEntity.ok(productDto);
    }

    @DeleteMapping("/{sku}")
    public ResponseEntity<?> removeBySku(@PathVariable String sku) {
        catalogServicesAdapter.removeProductBySkuUseCase(new RemoveProductBySkuCommand(sku));
        return ResponseEntity.noContent().build();
    }

    @PostMapping 
    public ResponseEntity<ProductDto> addProduct(@RequestBody AddProductCommand command) {
        return ResponseEntity.ok(catalogServicesAdapter.addProductUseCase(command));
    }

    @PatchMapping("/{sku}")
    public ResponseEntity<?> editProduct(@PathVariable("sku") String sku, @RequestBody EditCatalogInformationCommand editCatalogInformationCommand) {

        EditCatalogInformationCommand command = new EditCatalogInformationCommand(
            sku, 
            editCatalogInformationCommand.name(), 
            editCatalogInformationCommand.description(), 
            editCatalogInformationCommand.photo(), 
        editCatalogInformationCommand.price());

        catalogServicesAdapter.editCatalogInformationUseCase(command);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
