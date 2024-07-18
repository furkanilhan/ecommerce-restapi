package com.furkan.ecommerce.repository;

import com.furkan.ecommerce.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Page<User> findByIsDeletedFalse(Pageable pageable);
    User findByIdAndIsDeletedFalse(Long userId);
    User findByUsernameAndIsDeletedFalse(String username);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
    boolean existsByPhoneAndIdNot(String phone, Long userId);
    boolean existsByEmailAndIdNot(String email, Long userId);
}
