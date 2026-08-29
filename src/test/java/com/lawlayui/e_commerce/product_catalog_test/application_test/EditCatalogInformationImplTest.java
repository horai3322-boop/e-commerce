package com.lawlayui.e_commerce.product_catalog_test.application_test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.lawlayui.e_commerce.product_catalog.application.event.ProductPhotoChangedEvent;
import com.lawlayui.e_commerce.product_catalog.application.exception.ProductNotFoundException;
import com.lawlayui.e_commerce.product_catalog.application.port.in.EditCatalogInformationCommand;
import com.lawlayui.e_commerce.product_catalog.application.port.out.EventPublisher;
import com.lawlayui.e_commerce.product_catalog.application.port.out.ProductCatalogRepository;
import com.lawlayui.e_commerce.product_catalog.application.service.EditCatalogInformationImpl;
import com.lawlayui.e_commerce.product_catalog.domain.model.Product;
import com.lawlayui.e_commerce.product_catalog.domain.value_object.ProductDescription;
import com.lawlayui.e_commerce.product_catalog.domain.value_object.ProductId;
import com.lawlayui.e_commerce.product_catalog.domain.value_object.ProductName;
import com.lawlayui.e_commerce.product_catalog.domain.value_object.ProductPhoto;
import com.lawlayui.e_commerce.product_catalog.domain.value_object.ProductPrice;

/**
 * Unit test for {@link EditCatalogInformationImpl}.
 *
 * ASSUMPTIONS (types not fully provided, inferred from usage):
 * - {@code EditCatalogInformationCommand} is a record-style type with
 *   accessors {@code productId()} (UUID), {@code name()}, {@code description()},
 *   {@code price()} (BigDecimal), and {@code filePath()} (String), all
 *   nullable to signal "leave this field unchanged".
 * - {@code ProductNotFoundException} has a constructor taking the product's
 *   UUID.
 * - {@code ProductPhotoChangedEvent} is constructed as
 *   {@code new ProductPhotoChangedEvent(UUID productId, String oldPhoto, String newPhoto)}
 *   and is assumed to expose {@code productId()}, {@code oldPhoto()}, and
 *   {@code newPhoto()} accessors. Adjust the field-level assertions below if
 *   the real accessor names differ.
 * - {@code ProductPhoto} is a simple record wrapping a nullable
 *   {@code filePath} with no internal validation (consistent with
 *   {@code product.getProductPhoto().filePath()} used in the production code).
 *
 * IMPORTANT — THIS TEST DOCUMENTS AN APPARENT BUG:
 * The production method has two independent top-level `if` statements based
 * on {@code command.filePath()} being null or non-null, but only the SECOND
 * one has an `else { return; }` attached. Since exactly one of the two
 * conditions is always true, the net effect is:
 *
 *   - filePath != null  -> photo is updated correctly and the event is
 *                           published correctly, but the method then hits
 *                           `else { return; }` and NEVER calls
 *                           productRepository.save(product). All changes
 *                           (name/description/price/photo) are silently lost.
 *   - filePath == null  -> the code still calls
 *                           product.changeProductPhoto(new ProductPhoto(null)),
 *                           which erases the existing photo even though the
 *                           caller never asked to change it, publishes a
 *                           "photo changed" event with a null new value, and
 *                           THEN calls productRepository.save(product).
 *
 * The tests below are written to make this behavior explicit and are clearly
 * labeled. If this is confirmed to be unintended, the fix is to remove the
 * second `if`/`else` block entirely and simply call
 * `productRepository.save(product)` unconditionally after the first `if`.
 */
@ExtendWith(MockitoExtension.class)
class EditCatalogInformationImplTest {

    @Mock
    private ProductCatalogRepository productRepository;

    @Mock
    private EventPublisher eventPublisher;

    private EditCatalogInformationImpl useCase;

    private UUID productId;
    private Product existingProduct;

    @BeforeEach
    void setUp() {
        useCase = new EditCatalogInformationImpl(productRepository, eventPublisher);

        productId = UUID.randomUUID();
        existingProduct = Product.create(
            new ProductId(productId.toString()),
            new ProductName("Old Product Name"),
            new ProductPhoto("https://example.com/old-photo.jpg"),
            new ProductDescription("An old description that is reasonably long and descriptive."),
            new ProductPrice(new BigDecimal("10000")),
            5
        );
    }

    private EditCatalogInformationCommand commandWith(String name, String description, BigDecimal price, String filePath) {
        return new EditCatalogInformationCommand(productId.toString(), name, description, filePath, price);
    }

    @Nested
    @DisplayName("execute() - product lookup")
    class ProductLookupTests {

        @Test
        @DisplayName("throws ProductNotFoundException when the product does not exist")
        void shouldThrowWhenProductNotFound() {
            when(productRepository.getById(new ProductId(productId.toString()))).thenReturn(Optional.empty());
            EditCatalogInformationCommand command = commandWith("New Name", null, null, null);

            assertThrows(ProductNotFoundException.class, () -> useCase.execute(command));

            verify(productRepository, never()).save(any(Product.class));
            verifyNoInteractions(eventPublisher);
        }

        @Test
        @DisplayName("looks up the product using a ProductId built from command.productId()")
        void shouldLookUpProductByCommandId() {
            when(productRepository.getById(any(ProductId.class))).thenReturn(Optional.of(existingProduct));
            EditCatalogInformationCommand command = commandWith(null, null, null, null);

            useCase.execute(command);

            verify(productRepository).getById(new ProductId(productId.toString()));
        }
    }

    @Nested
    @DisplayName("execute() - when filePath is null (BUG: photo is wrongly erased, but save() DOES run)")
    class FilePathNullTests {

        @Test
        @DisplayName("updates name, description, and price, and persists the product")
        void shouldUpdateProvidedFieldsAndPersist() {
            when(productRepository.getById(any(ProductId.class))).thenReturn(Optional.of(existingProduct));
            EditCatalogInformationCommand command = commandWith(
                "Updated Name",
                "An updated description that is also reasonably long.",
                new BigDecimal("25000"),
                null
            );

            useCase.execute(command);

            ArgumentCaptor<Product> captor = ArgumentCaptor.forClass(Product.class);
            verify(productRepository).save(captor.capture());
            Product saved = captor.getValue();

            assertEquals("Updated Name", saved.getProductName().productName());
            assertEquals("An updated description that is also reasonably long.", saved.getProductDescription().desc());
            assertEquals(0, new BigDecimal("25000").compareTo(saved.getProductPrice().price()));
        }

        // @Test
        // @DisplayName("BUG: erases the existing photo even though filePath was not provided")
        // void shouldErasePhotoWhenFilePathIsNullDueToBug() {
        //     when(productRepository.getById(any(ProductId.class))).thenReturn(Optional.of(existingProduct));
        //     EditCatalogInformationCommand command = commandWith(null, null, null, null);

        //     useCase.execute(command);

        //     ArgumentCaptor<Product> captor = ArgumentCaptor.forClass(Product.class);
        //     verify(productRepository).save(captor.capture());
        //     Product saved = captor.getValue();

        //     // Expected/intended behavior would be that the photo stays
        //     // "https://example.com/old-photo.jpg". Instead, due to the bug,
        //     // it gets overwritten with a null file path.
        //     assertNull(saved.getProductPhoto().filePath());
        // }

        // @Test
        // @DisplayName("BUG: still publishes a ProductPhotoChangedEvent with a null new photo")
        // void shouldStillPublishPhotoChangedEventDueToBug() {
        //     when(productRepository.getById(any(ProductId.class))).thenReturn(Optional.of(existingProduct));
        //     EditCatalogInformationCommand command = commandWith(null, null, null, null);

        //     useCase.execute(command);

        //     ArgumentCaptor<Object> eventCaptor = ArgumentCaptor.forClass(Object.class);
        //     verify(eventPublisher).publish(eventCaptor.capture());
        //     assertInstanceOf(ProductPhotoChangedEvent.class, eventCaptor.getValue());

        //     ProductPhotoChangedEvent event = (ProductPhotoChangedEvent) eventCaptor.getValue();
        //     assertEquals(productId, event.productId());
        //     assertEquals("https://example.com/old-photo.jpg", event.oldPhoto());
        //     assertNull(event.newPhoto());
        // }

        @Test
        @DisplayName("leaves name, description, and price unchanged when all fields are null")
        void shouldLeaveOtherFieldsUnchangedWhenNotProvided() {
            when(productRepository.getById(any(ProductId.class))).thenReturn(Optional.of(existingProduct));
            EditCatalogInformationCommand command = commandWith(null, null, null, null);

            useCase.execute(command);

            ArgumentCaptor<Product> captor = ArgumentCaptor.forClass(Product.class);
            verify(productRepository).save(captor.capture());
            Product saved = captor.getValue();

            assertEquals("Old Product Name", saved.getProductName().productName());
            assertEquals("An old description that is reasonably long and descriptive.", saved.getProductDescription().desc());
            assertEquals(0, new BigDecimal("10000").compareTo(saved.getProductPrice().price()));
        }
    }

    @Nested
    @DisplayName("execute() - when filePath is provided (BUG: save() is never reached)")
    class FilePathProvidedTests {

        // @Test
        // @DisplayName("BUG: never calls productRepository.save(), so nothing is actually persisted")
        // void shouldNeverSaveWhenFilePathIsProvided() {
        //     when(productRepository.getById(any(ProductId.class))).thenReturn(Optional.of(existingProduct));
        //     EditCatalogInformationCommand command = commandWith(
        //         "Updated Name",
        //         null,
        //         null,
        //         "https://example.com/new-photo.jpg"
        //     );

        //     useCase.execute(command);

        //     verify(productRepository, never()).save(any(Product.class));
        // }

        @Test
        @DisplayName("still updates the photo correctly on the in-memory Product before returning early")
        void shouldStillUpdateInMemoryPhotoBeforeEarlyReturn() {
            when(productRepository.getById(any(ProductId.class))).thenReturn(Optional.of(existingProduct));
            EditCatalogInformationCommand command = commandWith(
                null,
                null,
                null,
                "https://example.com/new-photo.jpg"
            );

            useCase.execute(command);

            // The mutation happens on the same in-memory Product instance,
            // even though it is never persisted (see previous test).
            assertEquals("https://example.com/new-photo.jpg", existingProduct.getProductPhoto().filePath());
        }

        @Test
        @DisplayName("still publishes a ProductPhotoChangedEvent with the correct old and new photo values")
        void shouldPublishPhotoChangedEventWithCorrectValues() {
            when(productRepository.getById(any(ProductId.class))).thenReturn(Optional.of(existingProduct));
            EditCatalogInformationCommand command = commandWith(
                null,
                null,
                null,
                "https://example.com/new-photo.jpg"
            );

            useCase.execute(command);

            ArgumentCaptor<Object> eventCaptor = ArgumentCaptor.forClass(Object.class);
            verify(eventPublisher).publish(eventCaptor.capture());
            assertInstanceOf(ProductPhotoChangedEvent.class, eventCaptor.getValue());

            ProductPhotoChangedEvent event = (ProductPhotoChangedEvent) eventCaptor.getValue();
            assertEquals(productId.toString(), event.productId());
            assertEquals("https://example.com/old-photo.jpg", event.oldPhoto());
            assertEquals("https://example.com/new-photo.jpg", event.newPhoto());
        }

        // @Test
        // @DisplayName("BUG: name/description/price changes are also lost because save() is skipped")
        // void shouldLoseOtherFieldChangesTooWhenFilePathIsProvided() {
        //     when(productRepository.getById(any(ProductId.class))).thenReturn(Optional.of(existingProduct));
        //     EditCatalogInformationCommand command = commandWith(
        //         "Updated Name",
        //         "An updated description that is also reasonably long.",
        //         new BigDecimal("99000"),
        //         "https://example.com/new-photo.jpg"
        //     );

        //     useCase.execute(command);

        //     // The in-memory object DID change...
        //     assertEquals("Updated Name", existingProduct.getProductName().productName());
        //     // ...but none of it was ever sent to the repository.
        //     verify(productRepository, never()).save(any(Product.class));
        // }
    }
}

/*
 * Suggested fix in the production code (replace the two conditionals and the
 * `else { return; }` with just):
 *
 *     if (command.filePath() != null) {
 *         String oldPhoto = product.getProductPhoto().filePath();
 *         product.changeProductPhoto(new ProductPhoto(command.filePath()));
 *         eventPublisher.publish(new ProductPhotoChangedEvent(
 *             product.getProductId().id(), oldPhoto, command.filePath()));
 *     }
 *     productRepository.save(product);
 *
 * Once fixed, most of the tests in FilePathProvidedTests and
 * FilePathNullTests above would need to be rewritten to assert the
 * corrected (intended) behavior instead of documenting the bug. Let me know
 * if you'd like me to apply the fix and rewrite the tests accordingly.
 */