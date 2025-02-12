package com.furkan.ecommerce.repository;

import com.furkan.ecommerce.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    CartItem findByIdAndIsDeletedFalse(Long id);
}
