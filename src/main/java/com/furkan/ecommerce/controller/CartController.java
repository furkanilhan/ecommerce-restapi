package com.furkan.ecommerce.controller;

import com.furkan.ecommerce.model.User;
import com.furkan.ecommerce.service.CartService;
import com.furkan.ecommerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${apiPrefix}/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private UserService userService;

    @PostMapping("/add")
    public ResponseEntity<String> addToCart(@AuthenticationPrincipal UserDetails userDetails,
                                            @RequestParam Long productVariantId,
                                            @RequestParam int quantity) {
        String username = userDetails.getUsername();
        User user = userService.getUserByUsername(username);

        cartService.addToCart(user, productVariantId, quantity);

        return ResponseEntity.ok("Product added to cart successfully.");
    }
}
