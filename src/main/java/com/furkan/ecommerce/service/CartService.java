package com.furkan.ecommerce.service;

import com.furkan.ecommerce.model.Cart;
import com.furkan.ecommerce.model.CartItem;
import com.furkan.ecommerce.model.ProductVariant;
import com.furkan.ecommerce.model.User;
import com.furkan.ecommerce.repository.CartRepository;
import com.furkan.ecommerce.repository.ProductVariantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductVariantRepository productVariantRepository;

    public void addToCart(User user, Long productVariantId, int quantity) {
        Cart cart = user.getCart();

        if (cart == null) {
            cart = new Cart();
            cart.setUser(user);
            user.setCart(cart);
        }

        ProductVariant productVariant = productVariantRepository.findById(productVariantId)
                .orElseThrow(() -> new RuntimeException("Product variant not found"));

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

        cartRepository.save(cart);
    }
}

