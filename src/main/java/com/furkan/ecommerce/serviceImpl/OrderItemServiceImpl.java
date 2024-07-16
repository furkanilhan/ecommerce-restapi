package com.furkan.ecommerce.serviceImpl;

import com.furkan.ecommerce.model.OrderItem;
import com.furkan.ecommerce.repository.OrderItemRepository;
import com.furkan.ecommerce.service.OrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderItemServiceImpl implements OrderItemService {

    @Autowired
    private OrderItemRepository orderItemRepository;

    public void saveOrderItems(List<OrderItem> orderItems) {
        orderItemRepository.saveAll(orderItems);
    }
}
