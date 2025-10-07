package com.merx_commerce.demo.services;

import com.merx_commerce.demo.entities.Category;
import com.merx_commerce.demo.entities.DTOS.ProductRequestDTO;
import com.merx_commerce.demo.entities.DTOS.ProductResponseDTO;
import com.merx_commerce.demo.entities.Product;
import com.merx_commerce.demo.exceptions.AlreadyExistsException;
import com.merx_commerce.demo.exceptions.NotFoundException;
import com.merx_commerce.demo.repositories.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;


@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product p1,p2;

    private Category c1;

    @BeforeEach
    void setup(){
        p1 = new Product(1L,"Notebook", "A smart Notebook", new BigDecimal("1000"),5,List.of("link1","link2"),c1,List.of(),List.of());
        p2 = new Product(2L,"Mouse", "A smart Mouse", new BigDecimal("200"),2,List.of("link1","link2"),c1,List.of(),List.of());
        c1 = new Category(1L,"Electronics", List.of(p1,p2));

        p1.setCategory(c1);
        p2.setCategory(c1);
    }

    @Test
    @DisplayName("Test Find all Products SUCCESS")
    void testFindAllProducts(){
        when(productRepository.findAll()).thenReturn(List.of(p1,p2));
        List<ProductResponseDTO> products = productService.findAllProducts();

        Assertions.assertEquals(2, products.size());
    }

    @Test
    @DisplayName("Test find Product by Id SUCCESS")
    void testFindProductByIdSuccess(){
        when(productRepository.findById(anyLong())).thenReturn(Optional.ofNullable(p1));
        ProductResponseDTO product = productService.findById(1L);

        Assertions.assertEquals("Notebook", product.name());
    }

    @Test
    @DisplayName("Test find Product by Id FAILED")
    void testFindProductByIdFailed(){
        when(productRepository.findById(anyLong())).thenReturn(Optional.empty());
        NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () -> productService.findById(1L));

        Assertions.assertEquals("Not exist Product with this ID",exception.getMessage());
    }

    @Test
    @DisplayName("Test Create Product SUCCESS")
    void testCreateProductSuccess(){
        Product p3 = new Product(3L,"Keyboard", "A smart Keyboard", new BigDecimal("350"),2,List.of("link1","link2"),c1,List.of(),List.of());
        when(productRepository.existsByName(anyString())).thenReturn(false);
        when(productRepository.save(any(Product.class))).thenReturn(p3);
        ProductResponseDTO productDTO = productService.createProduct(new ProductRequestDTO(p3));

        Assertions.assertNotNull(productDTO);
        Assertions.assertEquals("Keyboard",productDTO.name());
    }

    @Test
    @DisplayName("Test Create Product FAILED")
    void testCreateProductFailed(){
        when(productRepository.existsByName(anyString())).thenReturn(true);
        AlreadyExistsException exception = Assertions.assertThrows(AlreadyExistsException.class, () -> productService.createProduct(new ProductRequestDTO(p1)));

        Assertions.assertEquals("already exists a product with this Name",exception.getMessage());
    }

    @Test
    @DisplayName("Test Update Product SUCCESS")
    void testUpdateProductSuccess(){
        when(productRepository.findById(anyLong())).thenReturn(Optional.ofNullable(p1));
        when(productRepository.existsByName(anyString())).thenReturn(false);
        when(productRepository.save(any(Product.class))).thenReturn(p1);
        ProductResponseDTO productResponseDTO = productService.updateProduct(new ProductRequestDTO(p2),1L);

        Assertions.assertNotNull(productResponseDTO);
        Assertions.assertEquals("Mouse", productResponseDTO.name());
    }

    @Test
    @DisplayName("Test Update Product FAILED")
    void testUpdateProductFailed(){
        when(productRepository.findById(anyLong())).thenReturn(Optional.empty());
        NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () -> productService.updateProduct(new ProductRequestDTO(p2),1L));

        Assertions.assertEquals("Not exist Product with this ID",exception.getMessage());
    }

    @Test
    @DisplayName("Test Update Product FAILED2")
    void testUpdateProductFailed2(){
        when(productRepository.findById(anyLong())).thenReturn(Optional.ofNullable(p1));
        when(productRepository.existsByName(anyString())).thenReturn(true);
        AlreadyExistsException exception = Assertions.assertThrows(AlreadyExistsException.class, () -> productService.updateProduct(new ProductRequestDTO(p2),1L));

        Assertions.assertEquals("already exists a product with this name",exception.getMessage());
    }

    @Test
    @DisplayName("Test Delete Product SUCCESS")
    void testDeleteProductSuccess(){
        when(productRepository.existsById(anyLong())).thenReturn(true);
        productService.deleteProduct(1L);

        verify(productRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Test Delete Product FAILED")
    void testDeleteProductFailed(){
        when(productRepository.existsById(anyLong())).thenReturn(false);
        NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () -> productService.deleteProduct(1L));

        Assertions.assertEquals("Not exist Product with this ID",exception.getMessage());
    }
}
