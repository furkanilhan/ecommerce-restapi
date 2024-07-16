package com.furkan.ecommerce.service;

import com.furkan.ecommerce.model.Cart;
import com.furkan.ecommerce.model.User;

import java.util.List;

public interface CartService {

    Cart findByUserId(Long userId);

    void save(Cart cart);

    void addToCart(User user, Long productVariantId, int quantity);

    void removeItemsFromCart(User user, List<Long> cartItemIds);

    Integer getAvailableQuantity(Long productVariantId);

}
