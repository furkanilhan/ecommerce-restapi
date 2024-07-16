package com.furkan.ecommerce.controller;

import com.furkan.ecommerce.exception.CustomException;
import com.furkan.ecommerce.model.User;
import com.furkan.ecommerce.payload.request.AddToCartRequest;
import com.furkan.ecommerce.payload.response.AvailableQuantityResponse;
import com.furkan.ecommerce.payload.response.CartAddResponse;
import com.furkan.ecommerce.payload.response.MessageResponse;
import com.furkan.ecommerce.service.CartService;
import com.furkan.ecommerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("${apiPrefix}/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private UserService userService;

    @PostMapping("/add")
    public ResponseEntity<CartAddResponse> addToCart(@AuthenticationPrincipal UserDetails userDetails,
                                                     @RequestBody List<AddToCartRequest> addToCartRequests) {
        if (addToCartRequests == null || addToCartRequests.isEmpty()) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "No items to add to cart.");
        }
        String username = userDetails.getUsername();
        User user = userService.getUserByUsername(username);

        List<Long> successfulAdditions = new ArrayList<>();
        List<Long> failedAdditions = new ArrayList<>();
        List<AvailableQuantityResponse> availableStock = new ArrayList<>();

        for (AddToCartRequest request : addToCartRequests) {
            try {
                cartService.addToCart(user, request.getProductVariantId(), request.getQuantity());
                successfulAdditions.add(request.getProductVariantId());
            } catch (CustomException ex) {
                failedAdditions.add(request.getProductVariantId());
                int availableQuantityForProduct = cartService.getAvailableQuantity(request.getProductVariantId());
                availableStock.add(new AvailableQuantityResponse(request.getProductVariantId(), availableQuantityForProduct));
            }
        }

        CartAddResponse response = new CartAddResponse(successfulAdditions, failedAdditions, availableStock);
        if (!failedAdditions.isEmpty()) {
            return ResponseEntity.badRequest().body(response);
        }

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/items")
    public ResponseEntity<MessageResponse> removeItemsFromCart(@AuthenticationPrincipal UserDetails userDetails,
                                                    @RequestBody List<Long> cartItemIds) {
        String username = userDetails.getUsername();
        User user = userService.getUserByUsername(username);

        cartService.removeItemsFromCart(user, cartItemIds);
        return ResponseEntity.ok(new MessageResponse("Items removed successfully."));
    }
}
