package com.furkan.ecommerce.serviceImpl;

import com.furkan.ecommerce.dto.*;
import com.furkan.ecommerce.mapper.ProductVariantMapper;
import com.furkan.ecommerce.model.Color;
import com.furkan.ecommerce.model.ProductVariant;
import com.furkan.ecommerce.repository.ProductVariantRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
public class ProductVariantServiceImplTest {

    @Mock
    private ProductVariantRepository productVariantRepository;

    @Mock
    private EntityManager entityManager;

    @Mock
    private ProductVariantMapper productVariantMapper;

    @InjectMocks
    private ProductVariantServiceImpl productVariantService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetProductVariantById() {
        Long productVariantId = 1L;
        ProductVariant productVariant = new ProductVariant();
        productVariant.setId(productVariantId);
        productVariant.setColor(new Color());
        productVariant.setQuantity(10);
        productVariant.setPrice(BigDecimal.valueOf(50.0));

        when(productVariantRepository.findByIdAndIsDeletedFalse(productVariantId)).thenReturn(productVariant);

        ProductVariantDTO mockProductVariantDTO = new ProductVariantDTO();
        mockProductVariantDTO.setId(productVariant.getId());

        when(productVariantMapper.toProductVariantDTO(productVariant)).thenReturn(mockProductVariantDTO);

        ProductVariantDTO result = productVariantService.getProductVariantById(productVariantId);

        assertNotNull(result);
        assertEquals(productVariantId, result.getId());
    }

    @Test
    void testFilterProductVariants() {
        ProductVariantFilterDTO filterDTO = new ProductVariantFilterDTO();
        filterDTO.setMinQuantity(5);

        List<ProductVariant> productVariants = new ArrayList<>();
        ProductVariant productVariant1 = new ProductVariant();
        productVariant1.setId(1L);
        productVariant1.setQuantity(10);
        productVariant1.setColor(new Color());

        ProductVariant productVariant2 = new ProductVariant();
        productVariant2.setId(2L);
        productVariant2.setQuantity(8);
        productVariant2.setColor(new Color());

        productVariants.add(productVariant1);
        productVariants.add(productVariant2);

        CriteriaBuilder cb = Mockito.mock(CriteriaBuilder.class);
        CriteriaQuery<ProductVariant> cq = Mockito.mock(CriteriaQuery.class);
        Root<ProductVariant> root = Mockito.mock(Root.class);
        TypedQuery<ProductVariant> query = Mockito.mock(TypedQuery.class);

        when(entityManager.getCriteriaBuilder()).thenReturn(cb);
        when(cb.createQuery(ProductVariant.class)).thenReturn(cq);
        when(cq.from(ProductVariant.class)).thenReturn(root);
        when(entityManager.createQuery(cq)).thenReturn(query);
        when(query.getResultList()).thenReturn(productVariants);

        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<ProductVariantDTO> result = productVariantService.filterProductVariants(filterDTO, pageRequest);

        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
    }

    @Test
    void testAddProductVariant() {
        ProductVariantDTO productVariantDTO = new ProductVariantDTO();
        productVariantDTO.setId(1L);
        productVariantDTO.setColor(new ColorDTO());
        productVariantDTO.setQuantity(10);
        productVariantDTO.setPrice(BigDecimal.valueOf(50.0));

        ProductVariant productVariant = new ProductVariant();
        productVariant.setId(1L);
        productVariant.setColor(new Color());
        productVariant.setQuantity(10);
        productVariant.setPrice(BigDecimal.valueOf(50.0));

        when(productVariantMapper.toProductVariant(productVariantDTO)).thenReturn(productVariant);
        when(productVariantMapper.toProductVariantDTO(productVariant)).thenReturn(productVariantDTO);

        when(productVariantRepository.save(any(ProductVariant.class))).thenReturn(productVariant);

        ProductVariantDTO result = productVariantService.addProductVariant(productVariantDTO);

        assertNotNull(result);
        assertEquals(productVariant.getId(), result.getId());
    }

    @Test
    void testUpdateProductVariant() {
        Long productVariantId = 1L;
        ProductVariantDTO updatedProductVariantDTO = new ProductVariantDTO();
        updatedProductVariantDTO.setId(productVariantId);
        updatedProductVariantDTO.setQuantity(15);

        ColorDTO colorDTO = new ColorDTO();
        colorDTO.setId(1L);
        updatedProductVariantDTO.setColor(colorDTO);

        VariantDTO variantDTO = new VariantDTO();
        variantDTO.setId(1L);
        updatedProductVariantDTO.setVariant(variantDTO);

        ProductVariant productVariant = new ProductVariant();
        productVariant.setId(productVariantId);
        productVariant.setQuantity(10);

        when(productVariantRepository.findByIdAndIsDeletedFalse(productVariantId)).thenReturn(productVariant);
        when(productVariantRepository.save(productVariant)).thenReturn(productVariant);
        when(productVariantMapper.toProductVariantDTO(productVariant)).thenReturn(updatedProductVariantDTO);

        ProductVariantDTO result = productVariantService.updateProductVariant(productVariantId, updatedProductVariantDTO);

        assertNotNull(result);
        assertEquals(updatedProductVariantDTO.getId(), result.getId());
        assertEquals(updatedProductVariantDTO.getQuantity(), result.getQuantity());
    }

    @Test
    void testDeleteProductVariant() {
        Long productVariantId = 1L;
        ProductVariant productVariant = new ProductVariant();
        productVariant.setId(productVariantId);

        when(productVariantRepository.findByIdAndIsDeletedFalse(productVariantId)).thenReturn(productVariant);

        assertDoesNotThrow(() -> productVariantService.deleteProductVariant(productVariantId));
    }
}
