package com.furkan.ecommerce.serviceImpl;

import com.furkan.ecommerce.dto.CustomerOrderDTO;
import com.furkan.ecommerce.enums.OrderStatus;
import com.furkan.ecommerce.exception.CustomException;
import com.furkan.ecommerce.mapper.CustomerOrderMapper;
import com.furkan.ecommerce.model.*;
import com.furkan.ecommerce.repository.CustomerOrderRepository;
import com.furkan.ecommerce.service.*;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
public class CustomerOrderServiceImplTest {

    @Mock
    private CustomerOrderRepository customerOrderRepository;

    @Mock
    private PaymentService paymentService;

    @Mock
    private CartService cartService;

    @Mock
    private OrderItemService orderItemService;

    @Mock
    private ProductVariantService productVariantService;

    @Mock
    private CustomerOrderMapper customerOrderMapper;

    @InjectMocks
    private CustomerOrderServiceImpl customerOrderService;

    private User user;
    private Cart cart;
    private ProductVariant productVariant;
    private CartItem cartItem;
    private CustomerOrder customerOrder;
    private OrderItem orderItem;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);

        cart = new Cart();
        cart.setId(1L);
        cart.setUser(user);

        productVariant = new ProductVariant();
        productVariant.setId(1L);
        productVariant.setPrice(BigDecimal.valueOf(100.0));
        productVariant.setQuantity(10);

        cartItem = new CartItem();
        cartItem.setId(1L);
        cartItem.setProductVariant(productVariant);
        cartItem.setQuantity(2);

        cart.setCartItems(new ArrayList<>(List.of(cartItem)));

        customerOrder = new CustomerOrder();
        customerOrder.setId(1L);
        customerOrder.setUser(user);
        customerOrder.setStatus(OrderStatus.CREATED);

        orderItem = new OrderItem();
        orderItem.setId(1L);
        orderItem.setCustomerOrder(customerOrder);
        orderItem.setProductVariant(productVariant);
        orderItem.setQuantity(2);
        orderItem.setPrice(BigDecimal.valueOf(200.0));

        customerOrder.setItems(List.of(orderItem));
        customerOrder.setTotalPrice(BigDecimal.valueOf(200.0));
    }

    @Test
    void testGetAllOrders() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<CustomerOrder> customerOrderPage = new PageImpl<>(List.of(customerOrder), pageable, 1);
        when(customerOrderRepository.findByIsDeletedFalseAndUserId(user.getId(), pageable)).thenReturn(customerOrderPage);
        when(customerOrderMapper.toCustomerOrderDTO(any())).thenReturn(new CustomerOrderDTO());

        Page<CustomerOrderDTO> result = customerOrderService.getAllOrders(user.getId(), pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void testCreateOrder() throws StripeException {
        PaymentIntent paymentIntent = new PaymentIntent();
        paymentIntent.setId("pi_12345");
        paymentIntent.setStatus("succeeded");

        when(cartService.findByUserId(user.getId())).thenReturn(cart);
        when(paymentService.createPaymentIntent(anyLong(), anyString())).thenReturn(paymentIntent);
        when(paymentService.confirmPayment(anyString())).thenReturn(paymentIntent);

        customerOrderService.createOrder(user);

        verify(customerOrderRepository, times(1)).save(any(CustomerOrder.class));
        verify(productVariantService, times(1)).decreaseProductVariantsQuantity(anyList());
        verify(cartService, times(1)).save(any(Cart.class));
    }

    @Test
    void testCreateOrderPaymentFailed() throws StripeException {
        PaymentIntent paymentIntent = new PaymentIntent();
        paymentIntent.setId("pi_12345");
        paymentIntent.setStatus("requires_payment_method");

        when(cartService.findByUserId(user.getId())).thenReturn(cart);
        when(paymentService.createPaymentIntent(anyLong(), anyString())).thenReturn(paymentIntent);
        when(paymentService.confirmPayment(anyString())).thenReturn(paymentIntent);

        CustomException exception = assertThrows(CustomException.class, () -> customerOrderService.createOrder(user));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatus());
        assertTrue(exception.getMessage().contains("Payment processing failed"));
    }

    @Test
    void testCancelOrder() {
        when(customerOrderRepository.findById(customerOrder.getId())).thenReturn(Optional.of(customerOrder));
        customerOrderService.cancelOrder(customerOrder.getId());
        assertEquals(OrderStatus.CANCELLED, customerOrder.getStatus());
        verify(orderItemService, times(1)).saveOrderItems(anyList());
        verify(customerOrderRepository, times(1)).save(any(CustomerOrder.class));
        verify(productVariantService, times(1)).increaseProductVariantsQuantity(anyList());
    }

    @Test
    void testCancelOrderNotFound() {
        when(customerOrderRepository.findById(customerOrder.getId())).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> customerOrderService.cancelOrder(customerOrder.getId()));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertTrue(exception.getMessage().contains("Order not found"));
    }

    @Test
    void testCancelOrderInvalidStatus() {
        customerOrder.setStatus(OrderStatus.DELIVERED);

        when(customerOrderRepository.findById(customerOrder.getId())).thenReturn(Optional.of(customerOrder));

        CustomException exception = assertThrows(CustomException.class, () -> customerOrderService.cancelOrder(customerOrder.getId()));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertTrue(exception.getMessage().contains("Cannot cancel order with status"));
    }

    @Test
    void testReturnOrder() {
        customerOrder.setStatus(OrderStatus.DELIVERED);

        when(customerOrderRepository.findById(customerOrder.getId())).thenReturn(Optional.of(customerOrder));

        customerOrderService.returnOrder(customerOrder.getId());
    }

    @Test
    void testReturnOrderNotFound() {
        when(customerOrderRepository.findById(customerOrder.getId())).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> customerOrderService.returnOrder(customerOrder.getId()));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertTrue(exception.getMessage().contains("Order not found"));
    }

    @Test
    void testReturnOrderInvalidStatus() {
        customerOrder.setStatus(OrderStatus.CREATED);

        when(customerOrderRepository.findById(customerOrder.getId())).thenReturn(Optional.of(customerOrder));

        CustomException exception = assertThrows(CustomException.class, () -> customerOrderService.returnOrder(customerOrder.getId()));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertTrue(exception.getMessage().contains("Cannot return order with status"));
    }
}
