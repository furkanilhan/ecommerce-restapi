package com.furkan.ecommerce.serviceImpl;

import com.furkan.ecommerce.exception.CustomException;
import com.furkan.ecommerce.model.OrderItem;
import com.furkan.ecommerce.repository.OrderItemRepository;
import com.furkan.ecommerce.service.OrderItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class OrderItemServiceImpl implements OrderItemService {

    @Autowired
    private OrderItemRepository orderItemRepository;

    public void saveOrderItems(List<OrderItem> orderItems) {
        try {
            orderItemRepository.saveAll(orderItems);
        } catch (DataAccessException e) {
            log.error("Failed to save order items: {}", e.getMessage());
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to save order items.");
        }
    }
}
