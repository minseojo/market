package demo.demo.service;

import demo.demo.domain.Product;
import demo.demo.excption.AccessDeniedException;
import demo.demo.repository.ProductRepository;
import demo.demo.utility.Time;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.OngoingStubbing;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private FileService fileService;

    @Mock
    private Time time;

    private ProductService productService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        productService = new ProductService(productRepository, fileService, time);
    }

    @Test
    public void 상품_주인인_경우() {
        // Given
        Product existingProduct = Product.builder()
                .ownerId(1100L)
                .build();
        Long productId = 1L;
        Long ownerId = 1100L;
        // When
        when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));
        boolean isOwner = productService.isProductOwner(ownerId, productId);
        // Then
        assertThat(isOwner).isTrue();
    }

    @Test
    public void 상품_주인이_아닌_경우() {
        // Given
        Product existingProduct = Product.builder()
                .ownerId(100L)
                .build();
        Long productId = 1L;
        Long ownerId = 2100L; // Different user ID
        // When
        when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));
        boolean isOwner = productService.isProductOwner(ownerId, productId);
        // Then
        assertThat(isOwner).isFalse();
    }

    @Test
    public void 상품이_존재하지_않는_경우() {
        // Given
        Long productId = 1L;
        Long ownerId = 100L;
        // When
        when(productRepository.findById(productId)).thenReturn(Optional.empty());
        // Then
        assertThrows(NoSuchElementException.class, () -> productService.isProductOwner(ownerId, productId));
    }
    @Test
    public void 상품_삭제_소유자인_경우() {
        // Given
        Product existingProduct = Product.builder()
                .ownerId(1200L)
                .build();
        existingProduct.setId(1L);

        Long validUserId = 1200L; // 같은 사용자
        Long productId = existingProduct.getId();

        // When
        when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));
        when(productRepository.deleteById(productId)).thenReturn(true); // 모킹된 값 반환
        boolean isDeleted = productService.delete(validUserId, productId);

        // Then
        assertThat(isDeleted).isTrue();
        verify(productRepository, times(1)).deleteById(productId); // deleteById 메서드가 호출되었는지 확인
    }



    @Test
    public void 상품_삭제_주인이_아닌_경우() {
        // Given
        Product notExistingProduct = Product.builder()
                .name("equalUserProduct")
                .price(1000)
                .category("나비")
                .createDate(time.getCurrentTime())
                .ownerId(100L) // 같은 사용자
                .build();
        notExistingProduct.setId(1123L);

        Long inValidUserId = 100L; // 다른 사용자
        Long productId = notExistingProduct.getId();

        // When
        when(productRepository.findById(productId)).thenReturn(Optional.of(notExistingProduct));
        when(productRepository.deleteById(productId)).thenReturn(true);
        boolean isDeleted = productService.delete(inValidUserId, productId);

        // then
        assertThat(isDeleted).isTrue();
        verify(productRepository, times(1)).deleteById(productId); // delete 메서드가

    }

}
