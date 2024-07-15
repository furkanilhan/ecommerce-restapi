package com.furkan.ecommerce.controller;

import com.furkan.ecommerce.service.PaymentService;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("${apiPrefix}/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/create")
    public ResponseEntity<Map<String, String>> createPayment(
            @RequestParam Long amount,
            @RequestParam String currency) {

        try {
            PaymentIntent paymentIntent = paymentService.createPaymentIntent(amount, currency);
            Map<String, String> responseData = new HashMap<>();
            responseData.put("paymentIntentId", paymentIntent.getId());
            responseData.put("clientSecret", paymentIntent.getClientSecret());
            return ResponseEntity.ok(responseData);
        } catch (StripeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}

