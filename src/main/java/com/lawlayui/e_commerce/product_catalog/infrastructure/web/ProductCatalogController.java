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
import com.lawlayui.e_commerce.product_catalog.application.port.in.AddProductUseCase;
import com.lawlayui.e_commerce.product_catalog.application.port.in.EditCatalogInformationCommand;
import com.lawlayui.e_commerce.product_catalog.application.port.in.EditCatalogInformationUseCase;
import com.lawlayui.e_commerce.product_catalog.application.port.in.GetProductByIdQuery;
import com.lawlayui.e_commerce.product_catalog.application.port.in.GetProductByIdUseCase;
import com.lawlayui.e_commerce.product_catalog.application.port.in.ProductDto;
import com.lawlayui.e_commerce.product_catalog.application.port.in.RemoveProductCommand;
import com.lawlayui.e_commerce.product_catalog.application.port.in.RemoveProductUseCase;
import com.lawlayui.e_commerce.product_catalog.application.port.in.SearchProductQuery;
import com.lawlayui.e_commerce.product_catalog.application.port.in.SearchProductUseCase;

@RestController
@RequestMapping("/api/v1/product-catalog")
public class ProductCatalogController {
    private final AddProductUseCase addProductUseCase;
    private final EditCatalogInformationUseCase editCatalogInformationUseCase;
    private final GetProductByIdUseCase getProductByIdUseCase;
    private final RemoveProductUseCase removeProductUseCase;
    private final SearchProductUseCase searchProductUseCase;

    public ProductCatalogController(AddProductUseCase addProductUseCase, EditCatalogInformationUseCase editCatalogInformationUseCase, GetProductByIdUseCase getProductByIdUseCase, RemoveProductUseCase removeProductUseCase, SearchProductUseCase searchProductUseCase) {
        this.addProductUseCase = addProductUseCase;
        this.editCatalogInformationUseCase = editCatalogInformationUseCase;
        this.getProductByIdUseCase = getProductByIdUseCase;
        this.removeProductUseCase = removeProductUseCase;
        this.searchProductUseCase = searchProductUseCase;
    }

    @GetMapping
    public ResponseEntity<List<ProductDto>> search(@RequestParam(defaultValue = "0") int page,
                                                   @RequestParam(defaultValue = "10") int pageSize,
                                                   @RequestParam(defaultValue = "") String searchKey) {
        List<ProductDto> products = searchProductUseCase.execute(new SearchProductQuery(page, pageSize, searchKey));
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductDto> getById(@PathVariable String productId) {
        ProductDto product = getProductByIdUseCase.execute(new GetProductByIdQuery(productId));
        return ResponseEntity.ok(product);
    }

    @PostMapping
    public ResponseEntity<ProductDto> addProduct(@RequestBody AddProductCommand productDto) {
        ProductDto product = addProductUseCase.execute(productDto);
        return ResponseEntity.ok(product);
    }

    @PatchMapping("/{productId}")
    public ResponseEntity<?> editProduct(@PathVariable String productId, @RequestBody EditCatalogInformationCommand editCatalogInformationCommand) {
        EditCatalogInformationCommand command = new EditCatalogInformationCommand(productId, editCatalogInformationCommand.name(), editCatalogInformationCommand.description(), editCatalogInformationCommand.filePath(),editCatalogInformationCommand.price());

        editCatalogInformationUseCase.execute(command);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<?> removeProduct(@PathVariable String productId) {
        removeProductUseCase.execute(new RemoveProductCommand(productId));
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
