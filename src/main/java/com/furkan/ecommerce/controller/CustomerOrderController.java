package com.furkan.ecommerce.controller;

import com.furkan.ecommerce.model.User;
import com.furkan.ecommerce.service.CustomerOrderService;
import com.furkan.ecommerce.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("${apiPrefix}/orders")
public class CustomerOrderController {

    @Autowired
    private CustomerOrderService customerOrderService;

    @Autowired
    private UserService userService;

    @PostMapping("/create")
    public ResponseEntity<String> createOrder(
            @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        User user = userService.getUserByUsername(username);

        try {
            customerOrderService.createOrder(user);
            return ResponseEntity.status(HttpStatus.CREATED).body("Your order has been successfully received.");
        } catch(Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to process your order. Please try again.");
        }
    }

    @PostMapping("/cancel/{orderId}")
    public ResponseEntity<String> cancelOrder(@PathVariable Long orderId) {
        ResponseEntity<String> response = customerOrderService.cancelOrder(orderId);
        return response;
    }

    @PostMapping("/return/{orderId}")
    public ResponseEntity<String> returnOrder(@PathVariable Long orderId) {
        ResponseEntity<String> response = customerOrderService.returnOrder(orderId);
        return response;
    }
}

