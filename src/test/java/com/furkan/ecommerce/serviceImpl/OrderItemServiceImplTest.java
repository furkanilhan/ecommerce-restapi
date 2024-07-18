package com.furkan.ecommerce.serviceImpl;

import com.furkan.ecommerce.exception.CustomException;
import com.furkan.ecommerce.model.OrderItem;
import com.furkan.ecommerce.model.ProductVariant;
import com.furkan.ecommerce.repository.OrderItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
public class OrderItemServiceImplTest {

    @InjectMocks
    private OrderItemServiceImpl orderItemService;

    @Mock
    private OrderItemRepository orderItemRepository;

    private OrderItem orderItem1;
    private OrderItem orderItem2;

    @BeforeEach
    public void setUp() {
        ProductVariant productVariant1 = new ProductVariant();
        productVariant1.setId(1L);
        productVariant1.setPrice(BigDecimal.valueOf(100));

        ProductVariant productVariant2 = new ProductVariant();
        productVariant2.setId(2L);
        productVariant2.setPrice(BigDecimal.valueOf(200));

        orderItem1 = new OrderItem();
        orderItem1.setId(1L);
        orderItem1.setProductVariant(productVariant1);
        orderItem1.setQuantity(2);
        orderItem1.setPrice(BigDecimal.valueOf(200));

        orderItem2 = new OrderItem();
        orderItem2.setId(2L);
        orderItem2.setProductVariant(productVariant2);
        orderItem2.setQuantity(1);
        orderItem2.setPrice(BigDecimal.valueOf(200));
    }

    @Test
    public void testSaveOrderItemsSuccess() {
        orderItemService.saveOrderItems(List.of(orderItem1, orderItem2));

        verify(orderItemRepository).saveAll(anyList());
    }

    @Test
    public void testSaveOrderItemsFailure() {
        doThrow(new DataAccessException("...") {}).when(orderItemRepository).saveAll(anyList());

        assertThrows(CustomException.class, () -> {
            orderItemService.saveOrderItems(List.of(orderItem1, orderItem2));
        });
    }
}
