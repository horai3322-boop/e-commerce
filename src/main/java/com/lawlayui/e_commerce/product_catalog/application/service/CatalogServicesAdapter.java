package com.lawlayui.e_commerce.product_catalog.application.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.lawlayui.e_commerce.product_catalog.application.port.in.AddProductCommand;
import com.lawlayui.e_commerce.product_catalog.application.port.in.ChangeCatalogStatusCommand;
import com.lawlayui.e_commerce.product_catalog.application.port.in.EditCatalogInformationCommand;
import com.lawlayui.e_commerce.product_catalog.application.port.in.GetProductBySkuQuery;
import com.lawlayui.e_commerce.product_catalog.application.port.in.ProductDto;
import com.lawlayui.e_commerce.product_catalog.application.port.in.RemoveProductBySkuCommand;
import com.lawlayui.e_commerce.product_catalog.application.port.in.SearchProductQuery;

@Component 
public class CatalogServicesAdapter {
    private final AddProductUseCase addProductUseCase;
    private final ChangeCatalogStatusUseCase changeCatalogStatusUseCase;
    private final EditCatalogInformationUseCase editCatalogInformationUseCase;
    private final GetProductBySkuUseCase getProductBySkuUseCase;
    private final RemoveProductBySkuUseCase removeProductBySkuUseCase;
    private final SearchProductUseCase searchProductUseCase;

    public CatalogServicesAdapter(AddProductUseCase addProductUseCase, 
        ChangeCatalogStatusUseCase changeCatalogStatusUseCase,
        EditCatalogInformationUseCase editCatalogInformationUseCase,
        GetProductBySkuUseCase getProductBySkuUseCase,
        RemoveProductBySkuUseCase removeProductBySkuUseCase,
        SearchProductUseCase searchProductUseCase
    ) {
        this.addProductUseCase = addProductUseCase;
        this.changeCatalogStatusUseCase = changeCatalogStatusUseCase;
        this.editCatalogInformationUseCase = editCatalogInformationUseCase;
        this.getProductBySkuUseCase = getProductBySkuUseCase;
        this.removeProductBySkuUseCase = removeProductBySkuUseCase;
        this.searchProductUseCase = searchProductUseCase;
    }

    public ProductDto addProductUseCase(AddProductCommand command) {
        return addProductUseCase.execute(command);
    }

    public void changeCatalogStatusUseCase(ChangeCatalogStatusCommand command) {
        changeCatalogStatusUseCase.execute(command);
    }

    public void editCatalogInformationUseCase(EditCatalogInformationCommand command) {
        editCatalogInformationUseCase.execute(command);
    }

    public ProductDto getProductBySkuUseCase(GetProductBySkuQuery query) {
        return getProductBySkuUseCase.execute(query);
    }

    public void removeProductBySkuUseCase(RemoveProductBySkuCommand command) {
        removeProductBySkuUseCase.execute(command);
    }

    public List<ProductDto> searchProductUseCase(SearchProductQuery query) {
        return searchProductUseCase.execute(query);
    }
}
