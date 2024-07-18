package com.furkan.ecommerce.serviceImpl;

import com.furkan.ecommerce.dto.*;
import com.furkan.ecommerce.exception.CustomException;
import com.furkan.ecommerce.mapper.ProductMapper;
import com.furkan.ecommerce.model.*;
import com.furkan.ecommerce.repository.ProductRepository;
import com.furkan.ecommerce.repository.ProductVariantRepository;
import com.furkan.ecommerce.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
public class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductVariantRepository productVariantRepository;

    @Mock
    private CategoryService categoryService;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductServiceImpl productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllProducts() {
        List<Product> products = new ArrayList<>();
        products.add(new Product());
        products.add(new Product());

        Page<Product> productPage = new PageImpl<>(products);

        when(productRepository.findByIsDeletedFalse(any())).thenReturn(productPage);
        when(productMapper.toProductDTO(any())).thenReturn(new ProductDTO());

        Page<ProductDTO> result = productService.getAllProducts(PageRequest.of(0, 10));

        assertNotNull(result);
        assertEquals(2, result.getContent().size());
    }

    @Test
    void testGetProductById() {
        Product product = new Product();
        product.setId(1L);
        product.setName("Test Product");

        when(productRepository.findByIdAndIsDeletedFalse(1L)).thenReturn(product);

        ProductDetailDTO mockProductDetailDTO = new ProductDetailDTO();
        mockProductDetailDTO.setId(product.getId());
        mockProductDetailDTO.setName(product.getName());

        when(productMapper.toProductDetailDTO(any())).thenReturn(mockProductDetailDTO);

        ProductDetailDTO result = productService.getProductById(1L);

        assertNotNull(result);
        assertEquals("Test Product", result.getName());
    }

    @Test
    void testGetProductByIdNotFound() {
        when(productRepository.findByIdAndIsDeletedFalse(1L)).thenReturn(null);

        CustomException exception = assertThrows(CustomException.class, () -> productService.getProductById(1L));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    void testAddProduct() {
        ProductDetailDTO productDTO = new ProductDetailDTO();
        productDTO.setName("Test Product");

        Product product = new Product();
        product.setId(1L);
        product.setName("Test Product");

        when(productMapper.toProduct(any())).thenReturn(product);
        when(productRepository.save(any())).thenReturn(product);
        when(productMapper.toProductDetailDTO(any())).thenReturn(productDTO);

        ProductDetailDTO result = productService.addProduct(productDTO);

        assertNotNull(result);
        assertEquals("Test Product", result.getName());
    }

    @Test
    void testUpdateProduct() {
        Long productId = 1L;
        ProductDetailDTO updatedProductDTO = new ProductDetailDTO();
        updatedProductDTO.setId(productId);
        updatedProductDTO.setName("Updated Product");

        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(1L);
        updatedProductDTO.setCategory(categoryDTO);

        ProductTypeDTO productTypeDTO = new ProductTypeDTO();
        productTypeDTO.setId(1L);
        updatedProductDTO.setProductType(productTypeDTO);

        BrandDTO brandDTO = new BrandDTO();
        brandDTO.setId(1L);
        updatedProductDTO.setBrand(brandDTO);

        BrandModelDTO brandModelDTO = new BrandModelDTO();
        brandModelDTO.setId(1L);
        updatedProductDTO.setBrandModel(brandModelDTO);

        Product product = new Product();
        product.setId(productId);
        product.setName("Original Product");

        when(productRepository.findByIdAndIsDeletedFalse(productId)).thenReturn(product);
        when(productRepository.save(any())).thenReturn(product);
        when(productMapper.toProductDetailDTO(any())).thenReturn(updatedProductDTO);

        ProductDetailDTO result = productService.updateProduct(productId, updatedProductDTO);

        assertNotNull(result);
        assertEquals("Updated Product", result.getName());
        assertEquals(1L, result.getCategory().getId()); // Güncellenmiş ürünün kategori ID'sini kontrol ediyoruz
    }

    @Test
    void testDeleteProduct() {
        Long productId = 1L;
        Product product = new Product();
        product.setId(productId);
        product.setName("Test Product");
        product.setProductVariants(new ArrayList<>());

        when(productRepository.findByIdAndIsDeletedFalse(productId)).thenReturn(product);

        assertDoesNotThrow(() -> productService.deleteProduct(productId));
    }
}

