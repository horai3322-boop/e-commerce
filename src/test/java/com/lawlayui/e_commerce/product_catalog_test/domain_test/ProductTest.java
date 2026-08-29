package com.lawlayui.e_commerce.product_catalog_test.domain_test;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.lawlayui.e_commerce.product_catalog.domain.model.Product;
import com.lawlayui.e_commerce.product_catalog.domain.value_object.ProductDescription;
import com.lawlayui.e_commerce.product_catalog.domain.value_object.ProductId;
import com.lawlayui.e_commerce.product_catalog.domain.value_object.ProductName;
import com.lawlayui.e_commerce.product_catalog.domain.value_object.ProductPhoto;
import com.lawlayui.e_commerce.product_catalog.domain.value_object.ProductPrice;
import com.lawlayui.e_commerce.product_catalog.domain.value_object.ProductStatus;

/**
* Unit test for {@link Product}.
*
* NOTE: The value object code (ProductId, ProductName, ProductPhoto,
* ProductDescription, ProductPrice) was not included in the initial request.
* This test assumes each VO has a single-parameter constructor and
* accessors matching those used in the Product class
* (productName(), desc(), price()). Adjust the constructor calls in the
* helper method below if the actual VO signatures differ.
*/
class ProductTest {

    private ProductId validId;
    private ProductName validName;
    private ProductPhoto validPhoto;
    private ProductDescription validDescription;
    private ProductPrice validPrice;

    @BeforeEach
    void setUp() {
        validId = ProductId.generateId();
        validName = new ProductName("Plain T-Shirt");
        validPhoto = new ProductPhoto("https://example.com/photo.jpg");
        validDescription = new ProductDescription("Cotton plain t-shirt");
        validPrice = new ProductPrice(new BigDecimal("50000"));
    }

    // ---------- create() ----------

    @Nested
    @DisplayName("create()")
    class CreateTests {

        @Test
        @DisplayName("stock > 0 results in AVALIABLE status")
        void shouldBeAvailableWhenStockIsPositive() {
            Product product = Product.create(validId, validName, validPhoto, validDescription, validPrice, 10);

            assertEquals(ProductStatus.AVALIABLE, product.getStatus());
            assertEquals(validId, product.getProductId());
            assertEquals(validName, product.getProductName());
            assertEquals(validPhoto, product.getProductPhoto());
            assertEquals(validDescription, product.getProductDescription());
            assertEquals(validPrice, product.getProductPrice());
        }

        @Test
        @DisplayName("stock of 0 results in NOT_AVALIABLE status")
        void shouldBeNotAvailableWhenStockIsZero() {
            Product product = Product.create(validId, validName, validPhoto, validDescription, validPrice, 0);

            assertEquals(ProductStatus.NOT_AVALIABLE, product.getStatus());
        }

        @Test
        @DisplayName("negative stock results in NOT_AVALIABLE status")
        void shouldBeNotAvailableWhenStockIsNegative() {
            Product product = Product.create(validId, validName, validPhoto, validDescription, validPrice, -5);

            assertEquals(ProductStatus.NOT_AVALIABLE, product.getStatus());
        }

        @Test
        @DisplayName("null productId throws NullPointerException")
        void shouldThrowWhenProductIdIsNull() {
            assertThrows(NullPointerException.class, () ->
                Product.create(null, validName, validPhoto, validDescription, validPrice, 10));
        }

        @Test
        @DisplayName("null productName throws NullPointerException")
        void shouldThrowWhenNameIsNull() {
            assertThrows(NullPointerException.class, () ->
                Product.create(validId, null, validPhoto, validDescription, validPrice, 10));
        }

        @Test
        @DisplayName("null productPhoto throws NullPointerException")
        void shouldThrowWhenPhotoIsNull() {
            assertThrows(NullPointerException.class, () ->
                Product.create(validId, validName, null, validDescription, validPrice, 10));
        }

        @Test
        @DisplayName("null productDescription throws NullPointerException")
        void shouldThrowWhenDescriptionIsNull() {
            assertThrows(NullPointerException.class, () ->
                Product.create(validId, validName, validPhoto, null, validPrice, 10));
        }

        @Test
        @DisplayName("null productPrice throws NullPointerException")
        void shouldThrowWhenPriceIsNull() {
            assertThrows(NullPointerException.class, () ->
                Product.create(validId, validName, validPhoto, validDescription, null, 10));
        }
    }

    // ---------- remove() ----------

    @Nested
    @DisplayName("remove()")
    class RemoveTests {

        @Test
        @DisplayName("without transaction history -> PENDING_DELETION status")
        void shouldSetPendingDeletionWhenNoTransactionHistory() {
            Product product = Product.create(validId, validName, validPhoto, validDescription, validPrice, 10);

            product.remove();

            assertEquals(ProductStatus.PENDING_DELETION, product.getStatus());
        }

        @Test
        @DisplayName("with transaction history -> ARCHIVED status")
        void shouldSetArchivedWhenHasTransactionHistory() {
            Product product = Product.create(validId, validName, validPhoto, validDescription, validPrice, 10);
            product.markAsTransacted();

            product.remove();

            assertEquals(ProductStatus.ARCHIVED, product.getStatus());
        }
    }

    // ---------- changeProductName() ----------

    @Nested
    @DisplayName("changeProductName()")
    class ChangeProductNameTests {

        @Test
        @DisplayName("valid name is changed successfully")
        void shouldChangeNameWhenValid() {
            Product product = Product.create(validId, validName, validPhoto, validDescription, validPrice, 10);
            ProductName newName = new ProductName("Plain T-Shirt New Edition");

            product.changeProductName(newName);

            assertEquals(newName, product.getProductName());
        }

        @Test
        @DisplayName("empty name throws IllegalArgumentException")
        void shouldThrowWhenNameIsEmpty() {
            Product product = Product.create(validId, validName, validPhoto, validDescription, validPrice, 10);
            ProductName emptyName = new ProductName("");

            assertThrows(IllegalArgumentException.class, () -> product.changeProductName(emptyName));
        }

        @Test
        @DisplayName("name longer than 255 characters throws IllegalArgumentException")
        void shouldThrowWhenNameExceedsMaxLength() {
            Product product = Product.create(validId, validName, validPhoto, validDescription, validPrice, 10);
            ProductName tooLongName = new ProductName("a".repeat(256));

            assertThrows(IllegalArgumentException.class, () -> product.changeProductName(tooLongName));
        }

        @Test
        @DisplayName("name of exactly 255 characters is accepted")
        void shouldAcceptNameAtMaxLength() {
            Product product = Product.create(validId, validName, validPhoto, validDescription, validPrice, 10);
            ProductName maxLengthName = new ProductName("a".repeat(255));

            product.changeProductName(maxLengthName);

            assertEquals(maxLengthName, product.getProductName());
        }
    }

    // ---------- changeProductPhoto() ----------

    @Nested
    @DisplayName("changeProductPhoto()")
    class ChangeProductPhotoTests {

        @Test
        @DisplayName("valid photo is changed successfully")
        void shouldChangePhotoWhenValid() {
            Product product = Product.create(validId, validName, validPhoto, validDescription, validPrice, 10);
            ProductPhoto newPhoto = new ProductPhoto("https://example.com/new-photo.jpg");

            product.changeProductPhoto(newPhoto);

            assertEquals(newPhoto, product.getProductPhoto());
        }

        @Test
        @DisplayName("null photo throws NullPointerException")
        void shouldThrowWhenPhotoIsNull() {
            Product product = Product.create(validId, validName, validPhoto, validDescription, validPrice, 10);

            assertThrows(NullPointerException.class, () -> product.changeProductPhoto(null));
        }
    }

    // ---------- changeProductDescription() ----------

    @Nested
    @DisplayName("changeProductDescription()")
    class ChangeProductDescriptionTests {

        @Test
        @DisplayName("valid description is changed successfully")
        void shouldChangeDescriptionWhenValid() {
            Product product = Product.create(validId, validName, validPhoto, validDescription, validPrice, 10);
            ProductDescription newDescription = new ProductDescription("A more complete new description");

            product.changeProductDescription(newDescription);

            assertEquals(newDescription, product.getProductDescription());
        }

        @Test
        @DisplayName("empty description throws IllegalArgumentException")
        void shouldThrowWhenDescriptionIsEmpty() {
            Product product = Product.create(validId, validName, validPhoto, validDescription, validPrice, 10);
            ProductDescription emptyDescription = new ProductDescription("");

            assertThrows(IllegalArgumentException.class, () -> product.changeProductDescription(emptyDescription));
        }

        @Test
        @DisplayName("description longer than 1000 characters throws IllegalArgumentException")
        void shouldThrowWhenDescriptionExceedsMaxLength() {
            Product product = Product.create(validId, validName, validPhoto, validDescription, validPrice, 10);
            ProductDescription tooLongDescription = new ProductDescription("a".repeat(1001));

            assertThrows(IllegalArgumentException.class, () -> product.changeProductDescription(tooLongDescription));
        }

        @Test
        @DisplayName("description of exactly 1000 characters is accepted")
        void shouldAcceptDescriptionAtMaxLength() {
            Product product = Product.create(validId, validName, validPhoto, validDescription, validPrice, 10);
            ProductDescription maxLengthDescription = new ProductDescription("a".repeat(1000));

            product.changeProductDescription(maxLengthDescription);

            assertEquals(maxLengthDescription, product.getProductDescription());
        }
    }

    // ---------- changeProductPrice() ----------

    @Nested
    @DisplayName("changeProductPrice()")
    class ChangeProductPriceTests {

        @Test
        @DisplayName("valid price is changed successfully")
        void shouldChangePriceWhenValid() {
            Product product = Product.create(validId, validName, validPhoto, validDescription, validPrice, 10);
            ProductPrice newPrice = new ProductPrice(new BigDecimal("75000"));

            product.changeProductPrice(newPrice);

            assertEquals(newPrice, product.getProductPrice());
        }

        @Test
        @DisplayName("zero price throws IllegalArgumentException")
        void shouldThrowWhenPriceIsZero() {
            Product product = Product.create(validId, validName, validPhoto, validDescription, validPrice, 10);
            ProductPrice zeroPrice = new ProductPrice(BigDecimal.ZERO);

            assertThrows(IllegalArgumentException.class, () -> product.changeProductPrice(zeroPrice));
        }

        // NOTE: in the original implementation, the productPrice.price().compareTo(ZERO)
        // check runs BEFORE Objects.requireNonNull, so calling this method with a null
        // argument throws a NullPointerException when productPrice.price() is invoked,
        // not from the requireNonNull call on the following line.
        @Test
        @DisplayName("null price throws NullPointerException")
        void shouldThrowWhenPriceIsNull() {
            Product product = Product.create(validId, validName, validPhoto, validDescription, validPrice, 10);

            assertThrows(NullPointerException.class, () -> product.changeProductPrice(null));
        }
    }

    // ---------- changeStatus() ----------

    @Nested
    @DisplayName("changeStatus()")
    class ChangeStatusTests {

        @Test
        @DisplayName("valid status is changed successfully")
        void shouldChangeStatusWhenValid() {
            Product product = Product.create(validId, validName, validPhoto, validDescription, validPrice, 10);

            product.changeStatus(ProductStatus.ARCHIVED);

            assertEquals(ProductStatus.ARCHIVED, product.getStatus());
        }

        @Test
        @DisplayName("null status throws NullPointerException")
        void shouldThrowWhenStatusIsNull() {
            Product product = Product.create(validId, validName, validPhoto, validDescription, validPrice, 10);

            assertThrows(NullPointerException.class, () -> product.changeStatus(null));
        }
    }

    // ---------- markAsTransacted() ----------

    @Nested
    @DisplayName("markAsTransacted()")
    class MarkAsTransactedTests {

        @Test
        @DisplayName("affects remove() outcome, resulting in ARCHIVED")
        void shouldAffectRemoveBehavior() {
            Product product = Product.create(validId, validName, validPhoto, validDescription, validPrice, 10);

            product.markAsTransacted();
            product.remove();

            assertEquals(ProductStatus.ARCHIVED, product.getStatus());
        }
    }
}
