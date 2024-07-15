package com.furkan.ecommerce.service;

import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentConfirmParams;
import com.stripe.param.PaymentIntentCreateParams;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    private static final String RETURN_URL = "https://www.example.com";
    private static final String PAYMENT_METHOD = "pm_card_visa";

    public PaymentIntent createPaymentIntent(Long amount, String currency) throws StripeException {
        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(amount * 100L)
                .setCurrency(currency)
                .setPaymentMethod(PAYMENT_METHOD)
                .setConfirmationMethod(PaymentIntentCreateParams.ConfirmationMethod.MANUAL)
                .build();

        return PaymentIntent.create(params);
    }

    public PaymentIntent confirmPayment(String paymentIntentId) throws StripeException {
        PaymentIntent resource = PaymentIntent.retrieve(paymentIntentId);
        PaymentIntentConfirmParams params =
                PaymentIntentConfirmParams.builder()
                        .setPaymentMethod(PAYMENT_METHOD)
                        .setReturnUrl(RETURN_URL)
                        .build();
        return resource.confirm(params);

    }
}

