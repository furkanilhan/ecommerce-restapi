package com.furkan.ecommerce.controller;

import com.furkan.ecommerce.dto.UserDTO;
import com.furkan.ecommerce.mapper.UserMapper;
import com.furkan.ecommerce.model.User;
import com.furkan.ecommerce.payload.response.MessageResponse;
import com.furkan.ecommerce.service.CustomerOrderService;
import com.furkan.ecommerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    @Autowired
    private UserMapper userMapper;

    @PostMapping("/create")
    public ResponseEntity<MessageResponse> createOrder(
            @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        UserDTO userDTO = userService.getUserByUsername(username);
        User user = userMapper.toUser(userDTO);
        customerOrderService.createOrder(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(new MessageResponse("Your order has been successfully received."));
    }

    @PostMapping("/cancel/{orderId}")
    public ResponseEntity<MessageResponse> cancelOrder(@PathVariable Long orderId) {
        customerOrderService.cancelOrder(orderId);
        return ResponseEntity.ok(new MessageResponse("Your order has been canceled."));
    }

    @PostMapping("/return/{orderId}")
    public ResponseEntity<MessageResponse> returnOrder(@PathVariable Long orderId) {
        customerOrderService.returnOrder(orderId);
        return ResponseEntity.ok(new MessageResponse("Your return request has been received."));
    }
}

