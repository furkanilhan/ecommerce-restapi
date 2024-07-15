package com.furkan.ecommerce.controller;

import com.furkan.ecommerce.model.User;
import com.furkan.ecommerce.service.CustomerOrderService;
import com.furkan.ecommerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

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

        ResponseEntity<String> response = customerOrderService.createOrder(user);
        return response;
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

