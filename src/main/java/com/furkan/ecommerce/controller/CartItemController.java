package com.furkan.ecommerce.controller;

import com.furkan.ecommerce.payload.response.MessageResponse;
import com.furkan.ecommerce.service.CartItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${apiPrefix}/cart-item")
public class CartItemController {

    @Autowired
    private CartItemService cartItemService;

    @PatchMapping("/{cartItemId}")
    public ResponseEntity<MessageResponse> updateCartItemQuantity(@PathVariable Long cartItemId, @RequestParam int quantity) {
        cartItemService.updateCartItemQuantity(cartItemId, quantity);
        return ResponseEntity.ok(new MessageResponse("Cart item quantity updated successfully."));
    }
}

