package com.furkan.ecommerce.repository;

import com.furkan.ecommerce.model.CustomerOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerOrderRepository extends JpaRepository<CustomerOrder, Long> {
    Page<CustomerOrder> findByIsDeletedFalseAndUserId(Long userId, Pageable pageable);
}
