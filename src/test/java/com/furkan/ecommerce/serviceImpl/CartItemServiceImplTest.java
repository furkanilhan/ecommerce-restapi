package com.furkan.ecommerce.serviceImpl;

import com.furkan.ecommerce.exception.CustomException;
import com.furkan.ecommerce.model.CartItem;
import com.furkan.ecommerce.model.ProductVariant;
import com.furkan.ecommerce.repository.CartItemRepository;
import com.furkan.ecommerce.repository.ProductVariantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
public class CartItemServiceImplTest {

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private ProductVariantRepository productVariantRepository;

    @InjectMocks
    private CartItemServiceImpl cartItemService;

    private CartItem cartItem;
    private ProductVariant productVariant;

    @BeforeEach
    void setUp() {
        cartItem = new CartItem();
        cartItem.setId(1L);
        cartItem.setQuantity(2);

        productVariant = new ProductVariant();
        productVariant.setId(1L);
        productVariant.setQuantity(10);
        productVariant.setReservedQuantity(2);
        productVariant.setPrice(BigDecimal.valueOf(100.0));
        cartItem.setProductVariant(productVariant);
    }

    @Test
    void testUpdateCartItemQuantity_Success() {
        int newQuantity = 4;

        when(cartItemRepository.findByIdAndIsDeletedFalse(cartItem.getId())).thenReturn(cartItem);
        when(productVariantRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(cartItemRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        cartItemService.updateCartItemQuantity(cartItem.getId(), newQuantity);

        assertEquals(newQuantity, cartItem.getQuantity());
        assertEquals(4, productVariant.getReservedQuantity());
    }

    @Test
    void testUpdateCartItemQuantity_CartItemNotFound() {
        long nonExistentCartItemId = 999L;

        when(cartItemRepository.findByIdAndIsDeletedFalse(nonExistentCartItemId)).thenReturn(null);

        CustomException exception = assertThrows(CustomException.class,
                () -> cartItemService.updateCartItemQuantity(nonExistentCartItemId, 4));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertTrue(exception.getMessage().contains("Cart item not found"));
    }

    @Test
    void testUpdateCartItemQuantity_ReservedQuantityLessThanZero() {
        int newQuantity = -1;

        when(cartItemRepository.findByIdAndIsDeletedFalse(cartItem.getId())).thenReturn(cartItem);

        CustomException exception = assertThrows(CustomException.class,
                () -> cartItemService.updateCartItemQuantity(cartItem.getId(), newQuantity));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertTrue(exception.getMessage().contains("Reserved quantity cannot be less than zero"));
    }

    @Test
    void testUpdateCartItemQuantity_CannotReserveMoreThanAvailableQuantity() {
        int newQuantity = 20;

        when(cartItemRepository.findByIdAndIsDeletedFalse(cartItem.getId())).thenReturn(cartItem);

        CustomException exception = assertThrows(CustomException.class,
                () -> cartItemService.updateCartItemQuantity(cartItem.getId(), newQuantity));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertTrue(exception.getMessage().contains("Cannot reserve more than available quantity"));
    }
}
