package com.furkan.ecommerce.service;

import com.furkan.ecommerce.dto.UserDTO;
import com.furkan.ecommerce.dto.UserDetailDTO;
import com.furkan.ecommerce.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    Page<UserDetailDTO> getAllUsers(Pageable pageable);

    UserDetailDTO getUserById(Long userId);

    UserDetailDTO updateUser(Long userId, UserDetailDTO userDetailDTO);

    String changePassword(Long userId, String currentPassword, String newPassword);

    Boolean checkByUsername(String username);

    Boolean checkByEmail(String email);

    boolean checkByPhoneAndIdNot(String phone, Long userId);

    boolean checkByEmailAndIdNot(String email, Long userId);

    void saveUser(User user);
}
