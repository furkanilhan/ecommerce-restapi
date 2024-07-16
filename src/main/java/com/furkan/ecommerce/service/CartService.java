package com.furkan.ecommerce.service;

import com.furkan.ecommerce.model.Cart;
import com.furkan.ecommerce.model.User;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CartService {

    Cart findByUserId(Long userId);

    void save(Cart cart);

    boolean addToCart(User user, Long productVariantId, int quantity);

    ResponseEntity<String> removeItemsFromCart(User user, List<Long> cartItemIds);

    Integer getAvailableQuantity(Long productVariantId);

}
