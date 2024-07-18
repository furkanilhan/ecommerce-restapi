package com.furkan.ecommerce.serviceImpl;

import com.furkan.ecommerce.exception.CustomException;
import com.furkan.ecommerce.model.*;
import com.furkan.ecommerce.repository.CartItemRepository;
import com.furkan.ecommerce.repository.CartRepository;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
public class CartServiceImplTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private ProductVariantRepository productVariantRepository;

    @InjectMocks
    private CartServiceImpl cartService;

    @Mock
    private CartItemRepository cartItemRepository;

    private User user;
    private ProductVariant productVariant;
    private Cart cart;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        cart = new Cart();
        cart.setUser(user);
        user.setCart(cart);

        productVariant = new ProductVariant();
        productVariant.setId(1L);
        productVariant.setQuantity(10);
        productVariant.setReservedQuantity(0);
        productVariant.setPrice(BigDecimal.valueOf(100.0));
    }

    @Test
    void testAddToCart_ProductVariantNotFound() {
        long nonExistentProductVariantId = 999L;

        when(productVariantRepository.findByIdAndIsDeletedFalse(nonExistentProductVariantId)).thenReturn(null);

        CustomException exception = assertThrows(CustomException.class,
                () -> cartService.addToCart(user, nonExistentProductVariantId, 1));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertTrue(exception.getMessage().contains("Product variant not found"));
    }

    @Test
    void testAddToCart_NotEnoughStock() {
        int quantity = 15;

        when(productVariantRepository.findByIdAndIsDeletedFalse(productVariant.getId())).thenReturn(productVariant);

        CustomException exception = assertThrows(CustomException.class,
                () -> cartService.addToCart(user, productVariant.getId(), quantity));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertTrue(exception.getMessage().contains("Not enough stock available"));
    }

    @Test
    void testAddToCart_Success() {
        int quantity = 3;

        when(productVariantRepository.findByIdAndIsDeletedFalse(productVariant.getId())).thenReturn(productVariant);
        when(cartRepository.findByUserId(user.getId())).thenReturn(Optional.of(cart));

        cartService.addToCart(user, productVariant.getId(), quantity);

        assertNotNull(cart.getCartItems());
        assertEquals(1, cart.getCartItems().size());
        CartItem cartItem = cart.getCartItems().get(0);
        assertEquals(productVariant, cartItem.getProductVariant());
        assertEquals(quantity, cartItem.getQuantity());
        assertEquals(quantity, productVariant.getReservedQuantity());
    }

    @Test
    void testRemoveItemsFromCart_Success() {
        CartItem cartItem = new CartItem();
        cartItem.setId(1L);
        cartItem.setCart(cart);
        cartItem.setProductVariant(productVariant);
        productVariant.setReservedQuantity(2);
        cartItem.setQuantity(2);

        List<CartItem> cartItems = new ArrayList<>();
        cartItems.add(cartItem);

        when(cartRepository.findByUserId(user.getId())).thenReturn(Optional.of(cart));
        when(cartItemRepository.findAllById(any())).thenReturn(cartItems);

        cartService.removeItemsFromCart(user, List.of(cartItem.getId()));

        assertTrue(cart.getCartItems().isEmpty());
        assertEquals(0, productVariant.getReservedQuantity());
    }

    @Test
    void testRemoveItemsFromCart_CartNotFound() {
        when(cartRepository.findByUserId(user.getId())).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class,
                () -> cartService.removeItemsFromCart(user, List.of(1L)));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertTrue(exception.getMessage().contains("Cart not found"));
    }

    @Test
    void testRemoveItemsFromCart_CartItemsNotFound() {
        when(cartRepository.findByUserId(user.getId())).thenReturn(Optional.of(cart));
        when(cartItemRepository.findAllById(any())).thenReturn(new ArrayList<>());

        CustomException exception = assertThrows(CustomException.class,
                () -> cartService.removeItemsFromCart(user, List.of(1L)));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertTrue(exception.getMessage().contains("Cart items not found"));
    }

    @Test
    void testRemoveItemsFromCart_ItemsNotBelongToUser() {
        CartItem cartItem = new CartItem();
        cartItem.setId(1L);
        User userNew = new User();
        userNew.setId(2L);
        Cart cartNew = new Cart();
        cartNew.setId(2L);
        cartNew.setUser(userNew);
        cartItem.setCart(cartNew);
        cartItem.setProductVariant(productVariant);
        cartItem.setQuantity(2);

        List<CartItem> cartItems = new ArrayList<>();
        cartItems.add(cartItem);

        when(cartRepository.findByUserId(user.getId())).thenReturn(Optional.of(cart));
        when(cartItemRepository.findAllById(any())).thenReturn(cartItems);

        CustomException exception = assertThrows(CustomException.class,
                () -> cartService.removeItemsFromCart(user, List.of(cartItem.getId())));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertTrue(exception.getMessage().contains("Some items do not belong to the user"));
    }

    @Test
    void testGetAvailableQuantity_Success() {
        int reservedQuantity = 2;
        productVariant.setReservedQuantity(reservedQuantity);

        when(productVariantRepository.findByIdAndIsDeletedFalse(productVariant.getId())).thenReturn(productVariant);

        int availableQuantity = cartService.getAvailableQuantity(productVariant.getId());

        assertEquals(productVariant.getQuantity() - reservedQuantity, availableQuantity);
    }

    @Test
    void testGetAvailableQuantity_ProductVariantNotFound() {
        long nonExistentProductVariantId = 999L;

        when(productVariantRepository.findByIdAndIsDeletedFalse(nonExistentProductVariantId)).thenReturn(null);

        CustomException exception = assertThrows(CustomException.class,
                () -> cartService.getAvailableQuantity(nonExistentProductVariantId));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertTrue(exception.getMessage().contains("Product variant not found"));
    }}
