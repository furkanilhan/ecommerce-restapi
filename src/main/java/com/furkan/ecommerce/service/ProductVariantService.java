package com.furkan.ecommerce.service;

import com.furkan.ecommerce.model.OrderItem;
import com.furkan.ecommerce.model.ProductVariant;
import com.furkan.ecommerce.repository.ProductVariantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ProductVariantService {

    @Autowired
    private ProductVariantRepository productVariantRepository;

    @Transactional
    public void deceraseProductVariantsQuantity(List<OrderItem> orderItems) {
        for (OrderItem orderItem : orderItems) {
            ProductVariant productVariant = orderItem.getProductVariant();
            int quantity = orderItem.getQuantity();

            if (productVariant.getQuantity() < quantity) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Insufficient stock for product variant: " + productVariant.getId());
            }

            productVariant.setQuantity(productVariant.getQuantity() - quantity);
            productVariant.setReservedQuantity(productVariant.getReservedQuantity() - quantity);

            productVariantRepository.save(productVariant);
        }
    }

    @Transactional
    public void increaseProductVariantsQuantity(List<OrderItem> orderItems) {
        for (OrderItem orderItem : orderItems) {
            ProductVariant productVariant = orderItem.getProductVariant();
            int quantity = orderItem.getQuantity();

            productVariant.setQuantity(productVariant.getQuantity() + quantity);

            productVariantRepository.save(productVariant);
        }
    }
}
