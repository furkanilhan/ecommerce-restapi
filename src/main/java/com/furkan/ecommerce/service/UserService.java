package com.furkan.ecommerce.service;

import com.furkan.ecommerce.model.Category;
import com.furkan.ecommerce.model.User;
import com.furkan.ecommerce.repository.CategoryRepository;
import com.furkan.ecommerce.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User getUserById(Long useryId) {
        return userRepository.findById(useryId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + useryId));
    }

    public User getUserByUsername(String userName) {
        return userRepository.findByUsername(userName)
                .orElseThrow(() -> new EntityNotFoundException("User not found with name: " + userName));
    }

    public Boolean checkByUsername(String userName) {
        return userRepository.existsByUsername(userName);
    }

    public Boolean checkByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }
}
