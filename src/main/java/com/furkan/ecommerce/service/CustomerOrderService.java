package com.furkan.ecommerce.service;

import com.furkan.ecommerce.dto.CustomerOrderDTO;
import com.furkan.ecommerce.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomerOrderService {

    Page<CustomerOrderDTO> getAllOrders(Long userId, Pageable pageable);

    void createOrder(User user);

    void cancelOrder(Long orderId);

    void returnOrder(Long orderId);

}
