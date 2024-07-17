package com.furkan.ecommerce.serviceImpl;

import com.furkan.ecommerce.enums.OrderStatus;
import com.furkan.ecommerce.exception.CustomException;
import com.furkan.ecommerce.model.*;
import com.furkan.ecommerce.repository.CustomerOrderRepository;
import com.furkan.ecommerce.service.*;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class CustomerOrderServiceImpl implements CustomerOrderService {

    private static final String PAYMENT_SUCCEEDED = "succeeded";
    private static final String CURRENCY = "TRY";

    @Autowired
    private CustomerOrderRepository customerOrderRepository;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private CartService cartService;

    @Autowired
    private OrderItemService orterItemService;

    @Autowired
    private ProductVariantService productVariantService;

    @Override
    @Transactional
    public void createOrder(User user) {
        Cart cart = cartService.findByUserId(user.getId());

        CustomerOrder customerOrder = new CustomerOrder();
        customerOrder.setUser(user);
        customerOrder.setStatus(OrderStatus.CREATED);

        BigDecimal totalPrice = BigDecimal.ZERO;
        List<OrderItem> orderItems = new ArrayList<>();

        for (CartItem cartItem : cart.getCartItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setCustomerOrder(customerOrder);
            orderItem.setProductVariant(cartItem.getProductVariant());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getProductVariant().getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())));

            orderItems.add(orderItem);
            totalPrice = totalPrice.add(cartItem.getProductVariant().getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())));
        }

        customerOrder.setItems(orderItems);
        customerOrder.setTotalPrice(totalPrice);

        try {
            PaymentIntent paymentIntent = paymentService.createPaymentIntent(totalPrice.longValue(), CURRENCY);

            paymentIntent = paymentService.confirmPayment(paymentIntent.getId());

            if (paymentIntent.getStatus().equals(PAYMENT_SUCCEEDED)){
                customerOrderRepository.save(customerOrder);
                productVariantService.decreaseProductVariantsQuantity(orderItems);

                cart.getCartItems().clear();
                cartService.save(cart);

            } else {
                throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "Payment processing failed for request: " + paymentIntent.getId());
            }
        } catch (StripeException e) {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to create order: Payment failed.", e);
        }
    }

    @Override
    @Transactional
    public void cancelOrder(Long orderId) {
        Optional<CustomerOrder> order = customerOrderRepository.findById(orderId);
        if (order.isEmpty()) {
            throw new CustomException(HttpStatus.NOT_FOUND, "Order not found.");
        }

        CustomerOrder customerOrder = order.get();

        if (customerOrder.getStatus() != OrderStatus.CREATED) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "Cannot cancel order with status: " + customerOrder.getStatus());
        }

        List<OrderItem> orderItems = customerOrder.getItems();

        orderItems.forEach(item -> {
            item.setDeleted(true);
        });

        customerOrder.setStatus(OrderStatus.CANCELLED);
        orterItemService.saveOrderItems(orderItems);
        customerOrderRepository.save(customerOrder);
        productVariantService.increaseProductVariantsQuantity(orderItems);
    }

    @Override
    @Transactional
    public void returnOrder(Long orderId) {
        Optional<CustomerOrder> optionalOrder = customerOrderRepository.findById(orderId);
        if (optionalOrder.isEmpty()) {
            throw new CustomException(HttpStatus.NOT_FOUND, "Order not found.");
        }

        CustomerOrder customerOrder = optionalOrder.get();

        if (customerOrder.getStatus() != OrderStatus.DELIVERED) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "Cannot return order with status: " + customerOrder.getStatus());
        }

        //TODO: sipariş iade işlemleri
    }

}

