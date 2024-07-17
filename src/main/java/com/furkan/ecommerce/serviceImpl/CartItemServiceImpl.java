package com.furkan.ecommerce.serviceImpl;

import com.furkan.ecommerce.exception.CustomException;
import com.furkan.ecommerce.model.CartItem;
import com.furkan.ecommerce.model.ProductVariant;
import com.furkan.ecommerce.repository.CartItemRepository;
import com.furkan.ecommerce.repository.ProductVariantRepository;
import com.furkan.ecommerce.service.CartItemService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CartItemServiceImpl implements CartItemService {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductVariantRepository productVariantRepository;

    @Override
    @Transactional
    public void updateCartItemQuantity(Long cartItemId, int newQuantity) {
        CartItem cartItem = cartItemRepository.findByIdAndIsDeletedFalse(cartItemId);
        if(cartItem == null) {
            throw new CustomException(HttpStatus.NOT_FOUND, "Cart item not found");
        }

        ProductVariant productVariant = cartItem.getProductVariant();

        int quantityChange = newQuantity - cartItem.getQuantity();
        int newReservedQuantity = productVariant.getReservedQuantity() + quantityChange;

        if (newReservedQuantity < 0) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "Reserved quantity cannot be less than zero");
        }
        if (newReservedQuantity > productVariant.getQuantity()) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "Cannot reserve more than available quantity");
        }

        productVariant.setReservedQuantity(newReservedQuantity);
        cartItem.setQuantity(newQuantity);

        try {
            productVariantRepository.save(productVariant);
            cartItemRepository.save(cartItem);
        } catch (Exception e) {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "Update quantity failed unexpectedly.", e);
        }
    }
}