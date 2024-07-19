package com.furkan.ecommerce.serviceImpl;

import com.furkan.ecommerce.exception.CustomException;
import com.furkan.ecommerce.model.*;
import com.furkan.ecommerce.repository.CartItemRepository;
import com.furkan.ecommerce.repository.CartRepository;
import com.furkan.ecommerce.repository.ProductVariantRepository;
import com.furkan.ecommerce.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductVariantRepository productVariantRepository;

    @Override
    public Cart findByUserId(Long userId) {
        return cartRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "Cart not found"));
    }

    @Override
    public void save(Cart cart) {
        cartRepository.save(cart);
    }

    @Override
    public void addToCart(User user, Long productVariantId, int quantity) {
        Cart cart = user.getCart();

        if (cart == null) {
            cart = new Cart();
            cart.setUser(user);
            user.setCart(cart);
        }

        ProductVariant productVariant = productVariantRepository.findByIdAndIsDeletedFalse(productVariantId);
        if (productVariant == null) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "Product variant not found: " + productVariantId);
        }

        int currentReservedQuantity = getCurrentReservedQuantity(productVariant);
        if (productVariant.getQuantity() - currentReservedQuantity < quantity) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "Not enough stock available.");
        }

        CartItem existingCartItem = cart.getCartItems().stream()
                .filter(item -> Objects.equals(item.getProductVariant().getId(), productVariantId))
                .findFirst().orElse(null);

        if (existingCartItem != null) {
            existingCartItem.setQuantity(existingCartItem.getQuantity() + quantity);
        } else {
            CartItem cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setProductVariant(productVariant);
            cartItem.setQuantity(quantity);
            cart.getCartItems().add(cartItem);
        }

        productVariant.setReservedQuantity(currentReservedQuantity + quantity);
        productVariantRepository.save(productVariant);
        cartRepository.save(cart);
    }

    @Override
    @Transactional
    public void removeItemsFromCart(User user, List<Long> cartItemIds) {
        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "Cart not found"));

        List<CartItem> cartItems = cartItemRepository.findAllById(cartItemIds);

        if (cartItems.isEmpty()) {
            throw new CustomException(HttpStatus.NOT_FOUND, "Cart items not found.");
        }

        List<CartItem> userCartItems = cartItems.stream()
                .filter(cartItem -> cartItem.getCart().getUser().getId().equals(user.getId()))
                .collect(Collectors.toList());

        if (userCartItems.size() != cartItems.size()) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "Some items do not belong to the user.");
        }

        userCartItems.forEach(cartItem -> {
            ProductVariant productVariant = cartItem.getProductVariant();
            productVariant.setReservedQuantity(productVariant.getReservedQuantity() - cartItem.getQuantity());
            productVariantRepository.save(productVariant);
        });

        try {
            cart.getCartItems().removeAll(userCartItems);
            cartItemRepository.deleteAll(userCartItems);
            cartRepository.save(cart);
        } catch (Exception e) {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "Remove items failed unexpectedly.", e);
        }
    }

    @Override
    public Integer getAvailableQuantity(Long productVariantId) {
        ProductVariant productVariant = productVariantRepository.findByIdAndIsDeletedFalse(productVariantId);
        if (productVariant == null) {
            throw new CustomException(HttpStatus.NOT_FOUND, "Product variant not found: " + productVariantId);
        }
        return productVariant.getQuantity() - getCurrentReservedQuantity(productVariant);
    }

    public Integer getCurrentReservedQuantity(ProductVariant productVariant) {
        return productVariant.getReservedQuantity() != null
                ? productVariant.getReservedQuantity() : 0;
    }
}

