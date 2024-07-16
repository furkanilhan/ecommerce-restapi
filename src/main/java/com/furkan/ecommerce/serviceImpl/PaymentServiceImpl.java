package com.furkan.ecommerce.serviceImpl;

import com.furkan.ecommerce.exception.CustomException;
import com.furkan.ecommerce.service.PaymentService;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentConfirmParams;
import com.stripe.param.PaymentIntentCreateParams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PaymentServiceImpl implements PaymentService {

    private static final String RETURN_URL = "https://www.example.com";
    private static final String PAYMENT_METHOD = "pm_card_visa";

    @Override
    public PaymentIntent createPaymentIntent(Long amount, String currency) {
        try {
            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                    .setAmount(amount * 100L)
                    .setCurrency(currency)
                    .setPaymentMethod(PAYMENT_METHOD)
                    .setConfirmationMethod(PaymentIntentCreateParams.ConfirmationMethod.MANUAL)
                    .build();

            return PaymentIntent.create(params);
        } catch (StripeException e) {
            handleStripeException(e);
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to create payment intent.");
        }
    }

    @Override
    public PaymentIntent confirmPayment(String paymentIntentId) {
        try {
            PaymentIntent paymentIntent = PaymentIntent.retrieve(paymentIntentId);
            PaymentIntentConfirmParams params = PaymentIntentConfirmParams.builder()
                    .setPaymentMethod(PAYMENT_METHOD)
                    .setReturnUrl(RETURN_URL)
                    .build();
            return paymentIntent.confirm(params);
        } catch (StripeException e) {
            handleStripeException(e);
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to confirm payment.");
        }
    }

    private void handleStripeException(StripeException e) {
        log.error("Stripe API error: {}", e.getMessage());
    }
}


