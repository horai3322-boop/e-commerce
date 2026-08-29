package com.lawlayui.e_commerce.product_catalog_test.application_test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.lawlayui.e_commerce.product_catalog.application.event.ProductCreatedEvent;
import com.lawlayui.e_commerce.product_catalog.application.mapper.ProductMapping;
import com.lawlayui.e_commerce.product_catalog.application.port.in.AddProductCommand;
import com.lawlayui.e_commerce.product_catalog.application.port.in.ProductDto;
import com.lawlayui.e_commerce.product_catalog.application.port.out.EventPublisher;
import com.lawlayui.e_commerce.product_catalog.application.port.out.ProductCatalogRepository;
import com.lawlayui.e_commerce.product_catalog.application.service.AddProductUseCaseImpl;
import com.lawlayui.e_commerce.product_catalog.domain.model.Product;
import com.lawlayui.e_commerce.product_catalog.domain.value_object.ProductStatus;

/**
 * Unit test for {@link AddProductUseCaseImpl}.
 *
 * ASSUMPTIONS (the referenced types were not fully provided, so this test
 * infers their shape from how they are used in AddProductUseCaseImpl,
 * AddProductCommand, and the JPA persistence classes):
 * - {@code ProductCatalogRepository}, {@code ProductMapping}, and
 *   {@code EventPublisher} are plain interfaces, so they can be mocked
 *   directly with Mockito.
 * - {@code ProductDto} is a type returned as-is by {@code ProductMapping},
 *   so this test only asserts the use case returns whatever the mapper
 *   produces — it does not assert on ProductDto's internal fields.
 * - {@code ProductCreatedEvent} is constructed as
 *   {@code new ProductCreatedEvent(UUID productId, int initialStock)} and is
 *   assumed to expose {@code productId()} and {@code initialStock()}
 *   accessors (record-style, matching the constructor call in
 *   AddProductUseCaseImpl).
 * - Value objects such as ProductName/ProductDescription/ProductPrice are
 *   assumed to be records that implement value-based equals(), matching the
 *   assumptions used in ProductTest.
 *
 * This test does NOT exercise Spring (no ApplicationContext, no
 * EventPublisherImpl, no JPA/database). It is a pure use-case/unit test.
 * See the notes at the end of this file for what an integration test
 * covering EventPublisherImpl and the JPA repository would additionally
 * need.
 */
@ExtendWith(MockitoExtension.class)
class AddProductUseCaseImplTest {

    @Mock
    private ProductCatalogRepository productRepository;

    @Mock
    private ProductMapping productMapping;

    @Mock
    private EventPublisher eventPublisher;

    private AddProductUseCaseImpl useCase;

    @BeforeEach
    void setUp() {
        useCase = new AddProductUseCaseImpl(productRepository, productMapping, eventPublisher);
    }

    private AddProductCommand validCommand(int initialStock) {
        return new AddProductCommand(
            "Plain T-Shirt",
            "A comfortable plain cotton t-shirt suitable for everyday casual wear.",
            new BigDecimal("50000"),
            "https://example.com/photo.jpg",
            initialStock
        );
    }

    @Nested
    @DisplayName("execute()")
    class ExecuteTests {

        @Test
        @DisplayName("saves a Product built from the command with AVALIABLE status when stock > 0")
        void shouldSaveProductWithAvailableStatusWhenStockIsPositive() {
            AddProductCommand command = validCommand(10);
            ProductDto expectedDto = mock(ProductDto.class);
            when(productMapping.toDto(any(Product.class))).thenReturn(expectedDto);

            ProductDto result = useCase.execute(command);

            ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);
            verify(productRepository).save(productCaptor.capture());
            Product savedProduct = productCaptor.getValue();

            assertEquals(command.name(), savedProduct.getProductName().productName());
            assertEquals(command.descirption(), savedProduct.getProductDescription().desc());
            assertEquals(0, command.price().compareTo(savedProduct.getProductPrice().price()));
            assertEquals(ProductStatus.AVALIABLE, savedProduct.getStatus());
            assertNotNull(savedProduct.getProductId());
            assertSame(expectedDto, result);
        }

        @Test
        @DisplayName("saves a Product with NOT_AVALIABLE status when initial stock is 0")
        void shouldSaveProductWithNotAvailableStatusWhenStockIsZero() {
            AddProductCommand command = validCommand(0);
            when(productMapping.toDto(any(Product.class))).thenReturn(mock(ProductDto.class));

            useCase.execute(command);

            ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);
            verify(productRepository).save(productCaptor.capture());
            assertEquals(ProductStatus.NOT_AVALIABLE, productCaptor.getValue().getStatus());
        }

        @Test
        @DisplayName("publishes a ProductCreatedEvent carrying the new product's id and the initial stock")
        void shouldPublishProductCreatedEventWithCorrectData() {
            AddProductCommand command = validCommand(25);
            when(productMapping.toDto(any(Product.class))).thenReturn(mock(ProductDto.class));

            useCase.execute(command);

            ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);
            verify(productRepository).save(productCaptor.capture());
            Product savedProduct = productCaptor.getValue();

            ArgumentCaptor<Object> eventCaptor = ArgumentCaptor.forClass(Object.class);
            verify(eventPublisher).publish(eventCaptor.capture());

            assertInstanceOf(ProductCreatedEvent.class, eventCaptor.getValue());
            ProductCreatedEvent publishedEvent = (ProductCreatedEvent) eventCaptor.getValue();

            assertEquals(savedProduct.getProductId().id(), publishedEvent.productId());
            assertEquals(command.initialStock(), publishedEvent.initialStock());
        }

        @Test
        @DisplayName("returns exactly what ProductMapping.toDto produces for the saved product")
        void shouldReturnDtoFromMapper() {
            AddProductCommand command = validCommand(5);
            ProductDto expectedDto = mock(ProductDto.class);
            when(productMapping.toDto(any(Product.class))).thenReturn(expectedDto);

            ProductDto result = useCase.execute(command);

            assertSame(expectedDto, result);

            ArgumentCaptor<Product> savedCaptor = ArgumentCaptor.forClass(Product.class);
            verify(productRepository).save(savedCaptor.capture());
            ArgumentCaptor<Product> mappedCaptor = ArgumentCaptor.forClass(Product.class);
            verify(productMapping).toDto(mappedCaptor.capture());

            // The same Product instance that gets persisted must be the one mapped to a DTO.
            assertSame(savedCaptor.getValue(), mappedCaptor.getValue());
        }

        @Test
        @DisplayName("saves the product and publishes the event in that order")
        void shouldSaveBeforePublishingEvent() {
            AddProductCommand command = validCommand(3);
            when(productMapping.toDto(any(Product.class))).thenReturn(mock(ProductDto.class));

            useCase.execute(command);

            InOrder inOrder = inOrder(productRepository, eventPublisher);
            inOrder.verify(productRepository).save(any(Product.class));
            inOrder.verify(eventPublisher).publish(any());
        }

        @Test
        @DisplayName("does not publish an event when saving the product fails")
        void shouldNotPublishEventWhenSaveFails() {
            AddProductCommand command = validCommand(3);
            when(productRepository.save(any(Product.class))).thenThrow(new RuntimeException("db unavailable"));

            assertThrows(RuntimeException.class, () -> useCase.execute(command));

            verifyNoInteractions(eventPublisher);
            verifyNoInteractions(productMapping);
        }

        @Test
        @DisplayName("assigns a freshly generated product id for every call")
        void shouldGenerateUniqueProductIdPerExecution() {
            when(productMapping.toDto(any(Product.class))).thenReturn(mock(ProductDto.class));

            useCase.execute(validCommand(1));
            useCase.execute(validCommand(1));

            ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);
            verify(productRepository, times(2)).save(productCaptor.capture());

            Product first = productCaptor.getAllValues().get(0);
            Product second = productCaptor.getAllValues().get(1);
            assertNotEquals(first.getProductId(), second.getProductId());
        }
    }
}

/*
 * OPTIONAL FOLLOW-UP TESTS (not implemented here, per the notes in the request):
 *
 * 1) EventPublisherImpl test — straightforward with Mockito, e.g.:
 *      ApplicationEventPublisher springPublisher = mock(ApplicationEventPublisher.class);
 *      EventPublisherImpl publisher = new EventPublisherImpl(springPublisher);
 *      publisher.publish(someEvent);
 *      verify(springPublisher).publishEvent(someEvent);
 *    A true "was the event actually published/handled" test would instead use
 *    @SpringBootTest (or @RecordApplicationEvents, available since Spring 5.3.3)
 *    with a real ApplicationContext and a test @EventListener/@TestComponent that
 *    records events it receives.
 *
 * 2) Repository/database integration test — use @DataJpaTest with an in-memory
 *    database (H2) to test JpaProductCatalogRepository + JpaProductCatalogEntityRepository
 *    + ProductPersistentMapper end-to-end (save/getById/getAll/delete), independent
 *    of AddProductUseCaseImpl. Let me know if you'd like this written out.
 *
 * 3) AddProductCommand validation test — a plain JUnit test asserting that the
 *    compact constructor throws ProductLengthException / ProductDataEmptyException /
 *    ProductPriceZeroException for each invalid field (name < 3 chars, empty name,
 *    description < 50 chars, empty description, zero price, empty file_path,
 *    negative initialStock). Let me know if you'd like this as a separate file too.
 */