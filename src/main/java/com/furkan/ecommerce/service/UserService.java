package com.furkan.ecommerce.service;

import com.furkan.ecommerce.dto.UserDTO;
import com.furkan.ecommerce.model.User;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    UserDTO getUserById(Long userId);

    UserDTO getUserByUsername(String username);

    Boolean checkByUsername(String username);

    Boolean checkByEmail(String email);

    void saveUser(User user);
}
