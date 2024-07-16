package com.furkan.ecommerce.service;

import com.furkan.ecommerce.exception.CustomException;
import com.furkan.ecommerce.model.User;
import com.furkan.ecommerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User getUserById(Long useryId) {
        return userRepository.findById(useryId)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "User not found with id: " + useryId));
    }

    public User getUserByUsername(String userName) {
        return userRepository.findByUsername(userName)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "User not found with name: " + userName));
    }

    public Boolean checkByUsername(String userName) {
        return userRepository.existsByUsername(userName);
    }

    public Boolean checkByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public void saveUser(User user) {
        try {
            userRepository.save(user);
        } catch (Exception ex) {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred while saving user.");
        }
    }
}
