package com.furkan.ecommerce.serviceImpl;

import com.furkan.ecommerce.dto.UserDTO;
import com.furkan.ecommerce.exception.CustomException;
import com.furkan.ecommerce.mapper.UserMapper;
import com.furkan.ecommerce.model.User;
import com.furkan.ecommerce.repository.UserRepository;
import com.furkan.ecommerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDTO getUserById(Long userId) {
        User user =  userRepository.findByIdAndIsDeletedFalse(userId);
        if (user == null) {
            throw new CustomException(HttpStatus.NOT_FOUND, "User not found with id: " + userId);
        }
        return userMapper.toUserDTO(user);
    }

    @Override
    public UserDTO getUserByUsername(String username) {
        User user = userRepository.findByUsernameAndIsDeletedFalse(username);
        if (user == null) {
            throw new CustomException(HttpStatus.NOT_FOUND, "User not found with username: " + username);
        }
        return userMapper.toUserDTO(user);
    }

    @Override
    public Boolean checkByUsername(String userName) {
        return userRepository.existsByUsername(userName);
    }

    @Override
    public Boolean checkByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public void saveUser(User user) {
        try {
            userRepository.save(user);
        } catch (Exception ex) {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred while saving user.");
        }
    }

}
