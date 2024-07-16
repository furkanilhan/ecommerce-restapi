package com.furkan.ecommerce.service;

import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;

public interface PaymentService {

    PaymentIntent createPaymentIntent(Long amount, String currency) throws StripeException;

    PaymentIntent confirmPayment(String paymentIntentId) throws StripeException;
}

