package com.furkan.ecommerce.repository;

import com.furkan.ecommerce.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByIdAndIsDeletedFalse(Long userId);
    User findByUsernameAndIsDeletedFalse(String username);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
}
