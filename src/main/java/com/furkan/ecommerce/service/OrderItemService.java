package com.furkan.ecommerce.service;

import com.furkan.ecommerce.model.OrderItem;

import java.util.List;

public interface OrderItemService {

    void saveOrderItems(List<OrderItem> orderItems);

}

