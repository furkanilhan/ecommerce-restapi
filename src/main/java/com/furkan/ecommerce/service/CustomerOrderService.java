package com.furkan.ecommerce.service;

import com.furkan.ecommerce.model.User;
import org.springframework.http.ResponseEntity;

public interface CustomerOrderService {

    void createOrder(User user);

    void cancelOrder(Long orderId);

    void returnOrder(Long orderId);

}
