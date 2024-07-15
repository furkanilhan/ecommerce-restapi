package com.furkan.ecommerce.service;

import com.furkan.ecommerce.model.Cart;
import com.furkan.ecommerce.model.CartItem;
import com.furkan.ecommerce.model.ProductVariant;
import com.furkan.ecommerce.model.User;
import com.furkan.ecommerce.repository.CartItemRepository;
import com.furkan.ecommerce.repository.CartRepository;
import com.furkan.ecommerce.repository.ProductVariantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductVariantRepository productVariantRepository;

    public boolean addToCart(User user, Long productVariantId, int quantity) {
        Cart cart = user.getCart();

        if (cart == null) {
            cart = new Cart();
            cart.setUser(user);
            user.setCart(cart);
        }

        ProductVariant productVariant = productVariantRepository.findById(productVariantId)
                .orElseThrow(() -> new RuntimeException("Product variant not found"));

        int currentReservedQuantity = getCurrentReservedQuantity(productVariant);
        if (productVariant.getQuantity() - currentReservedQuantity < quantity) {
            return false;
        }

        CartItem existingCartItem = cart.getCartItems().stream()
                .filter(item -> item.getProductVariant().equals(productVariant))
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
        return true;
    }

    @Transactional
    public ResponseEntity<String> removeItemsFromCart(User user, List<Long> cartItemIds) {
        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cart not found"));

        List<CartItem> cartItems = cartItemRepository.findAllById(cartItemIds);

        List<CartItem> userCartItems = cartItems.stream()
                .filter(cartItem -> cartItem.getCart().getUser().getId().equals(user.getId()))
                .collect(Collectors.toList());

        if (userCartItems.size() != cartItems.size()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Some items do not belong to the user.");
        }

        userCartItems.forEach(cartItem -> {
            ProductVariant productVariant = cartItem.getProductVariant();
            productVariant.setReservedQuantity(productVariant.getReservedQuantity() - cartItem.getQuantity());
            productVariantRepository.save(productVariant);
        });

        cart.getCartItems().removeAll(userCartItems);
        cartItemRepository.deleteAll(userCartItems);
        cartRepository.save(cart);
        return ResponseEntity.ok("Items removed successfully.");
    }

    public Integer getAvailableQuantity(Long productVariantId) {
        ProductVariant productVariant = productVariantRepository.findById(productVariantId)
                .orElseThrow(() -> new RuntimeException("Product variant not found"));

        return productVariant.getQuantity() - getCurrentReservedQuantity(productVariant);
    }

    public Integer getCurrentReservedQuantity(ProductVariant productVariant) {
        return productVariant.getReservedQuantity() != null
                ? productVariant.getReservedQuantity() : 0;
    }
}

