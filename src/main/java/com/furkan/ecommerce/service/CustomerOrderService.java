package com.furkan.ecommerce.service;

import com.furkan.ecommerce.enums.OrderStatus;
import com.furkan.ecommerce.model.*;
import com.furkan.ecommerce.repository.CustomerOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerOrderService {

    @Autowired
    private CustomerOrderRepository customerOrderRepository;

    @Autowired
    private CartService cartService;

    @Autowired
    private OrderItemService orterItemService;

    @Autowired
    private ProductVariantService productVariantService;

    @Transactional
    public ResponseEntity<String> createOrder(User user) {
        Cart cart = cartService.findByUserId(user.getId());

        CustomerOrder customerOrder = new CustomerOrder();
        customerOrder.setUser(user);
        customerOrder.setStatus(OrderStatus.CREATED);

        BigDecimal totalPrice = BigDecimal.ZERO;
        List<OrderItem> orderItems = new ArrayList<>();

        for (CartItem cartItem : cart.getCartItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setCustomerOrder(customerOrder);
            orderItem.setProductVariant(cartItem.getProductVariant());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getProductVariant().getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())));

            orderItems.add(orderItem);
            totalPrice = totalPrice.add(cartItem.getProductVariant().getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())));
        }

        customerOrder.setItems(orderItems);
        customerOrder.setTotalPrice(totalPrice);

        customerOrderRepository.save(customerOrder);

        productVariantService.deceraseProductVariantsQuantity(orderItems);

        cart.getCartItems().clear();
        cartService.save(cart);

        return ResponseEntity.ok("Order created successfully.");
    }

    @Transactional
    public ResponseEntity<String> cancelOrder(Long orderId) {
        Optional<CustomerOrder> order = customerOrderRepository.findById(orderId);
        if (order.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order not found.");
        }

        CustomerOrder customerOrder = order.get();

        if (customerOrder.getStatus() != OrderStatus.CREATED) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cannot cancel order with status: " + customerOrder.getStatus());
        }

        List<OrderItem> orderItems = customerOrder.getItems();

        orderItems.forEach(item -> {
            item.setDeleted(true);
            item.setDeletedAt(new Timestamp(System.currentTimeMillis()));
        });

        customerOrder.setStatus(OrderStatus.CANCELLED);
        orterItemService.saveOrderItems(orderItems);
        customerOrderRepository.save(customerOrder);
        productVariantService.increaseProductVariantsQuantity(orderItems);

        return ResponseEntity.ok("Order cancelled successfully.");
    }

    @Transactional
    public ResponseEntity<String> returnOrder(Long orderId) {
        Optional<CustomerOrder> optionalOrder = customerOrderRepository.findById(orderId);
        if (optionalOrder.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("CustomerOrder not found.");
        }

        CustomerOrder customerOrder = optionalOrder.get();

        if (customerOrder.getStatus() != OrderStatus.DELIVERED) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cannot return customerOrder with status: " + customerOrder.getStatus());
        }

        return ResponseEntity.ok("CustomerOrder returned successfully.");
    }

}

