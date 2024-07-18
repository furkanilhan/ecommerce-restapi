package com.furkan.ecommerce.serviceImpl;

import com.furkan.ecommerce.exception.CustomException;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentConfirmParams;
import com.stripe.param.PaymentIntentCreateParams;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
public class PaymentServiceImplTest {

    @InjectMocks
    private PaymentServiceImpl paymentService;

    @Mock
    private PaymentIntent paymentIntentMock;

    @Test
    public void testCreatePaymentIntentFailure() {
        try (MockedStatic<PaymentIntent> mockedPaymentIntentStatic = mockStatic(PaymentIntent.class)) {
            mockedPaymentIntentStatic.when(() -> PaymentIntent.create(any(PaymentIntentCreateParams.class)))
                    .thenThrow(mock(StripeException.class));

            assertThrows(CustomException.class, () -> paymentService.createPaymentIntent(100L, "usd"));
        }
    }

    @Test
    public void testConfirmPaymentSuccess() throws StripeException {
        try (MockedStatic<PaymentIntent> mockedPaymentIntentStatic = mockStatic(PaymentIntent.class)) {
            mockedPaymentIntentStatic.when(() -> PaymentIntent.retrieve(anyString())).thenReturn(paymentIntentMock);
            when(paymentIntentMock.confirm(any(PaymentIntentConfirmParams.class))).thenReturn(paymentIntentMock);

            paymentService.confirmPayment("pi_123");
            mockedPaymentIntentStatic.verify(() -> PaymentIntent.retrieve(anyString()));
            verify(paymentIntentMock, times(1)).confirm(any(PaymentIntentConfirmParams.class));
        }
    }

    @Test
    public void testConfirmPaymentFailure() {
        try (MockedStatic<PaymentIntent> mockedPaymentIntentStatic = mockStatic(PaymentIntent.class)) {
            mockedPaymentIntentStatic.when(() -> PaymentIntent.retrieve(anyString())).thenReturn(paymentIntentMock);
            when(paymentIntentMock.confirm(any(PaymentIntentConfirmParams.class)))
                    .thenThrow(mock(StripeException.class));

            assertThrows(CustomException.class, () -> paymentService.confirmPayment("pi_123"));
        } catch (StripeException e) {
            throw new RuntimeException(e);
        }
    }
}
