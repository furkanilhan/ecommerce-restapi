package com.furkan.ecommerce.serviceImpl;

import com.furkan.ecommerce.config.security.jwt.JwtUtils;
import com.furkan.ecommerce.dto.UserDetailDTO;
import com.furkan.ecommerce.exception.CustomException;
import com.furkan.ecommerce.mapper.UserMapper;
import com.furkan.ecommerce.model.User;
import com.furkan.ecommerce.repository.UserRepository;
import com.furkan.ecommerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public Page<UserDetailDTO> getAllUsers(Pageable pageable) {
        Page<User> usersPage = userRepository.findByIsDeletedFalse(pageable);
        return usersPage.map(userMapper::toUserDetailDTO);
    }

    @Override
    public UserDetailDTO getUserById(Long userId) {
        User user =  userRepository.findByIdAndIsDeletedFalse(userId);
        if (user == null) {
            throw new CustomException(HttpStatus.NOT_FOUND, "User not found with id: " + userId);
        }
        return userMapper.toUserDetailDTO(user);
    }

    @Override
    @Transactional
    public UserDetailDTO updateUser(Long userId, UserDetailDTO updatedUserDTO) {
        User user = userRepository.findByIdAndIsDeletedFalse(userId);
        if (user == null) {
            throw new CustomException(HttpStatus.NOT_FOUND, "User not found with id: " + userId);
        }

        if ((updatedUserDTO.getEmail() == null || updatedUserDTO.getEmail().isEmpty()) && (user.getEmail() == null || user.getEmail().isEmpty())) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "Email cannot be empty");
        }
        if (updatedUserDTO.getEmail() != null && !updatedUserDTO.getEmail().isEmpty()) {
            if (checkByEmailAndIdNot(updatedUserDTO.getEmail(), user.getId())) {
                throw new CustomException(HttpStatus.BAD_REQUEST, "Email address is in use.");
            }
            user.setEmail(updatedUserDTO.getEmail());
        }
        if (updatedUserDTO.getPhone() != null && !updatedUserDTO.getPhone().isEmpty()) {
            if (checkByPhoneAndIdNot(updatedUserDTO.getPhone(), user.getId())) {
                throw new CustomException(HttpStatus.BAD_REQUEST, "Phone number is in use.");
            }
            user.setPhone(updatedUserDTO.getPhone());
        }
        user.setPhone(updatedUserDTO.getPhone());
        user.setFirstName(updatedUserDTO.getFirstName());
        user.setLastName(updatedUserDTO.getLastName());

        try {
            User updatedUser = userRepository.save(user);
            return userMapper.toUserDetailDTO(updatedUser);
        } catch (Exception e) {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "User update failed unexpectedly.", e);
        }
    }

    @Override
    @Transactional
    public String changePassword(Long userId, String currentPassword, String newPassword) {
        User user = userRepository.findByIdAndIsDeletedFalse(userId);
        if (user == null) {
            throw new CustomException(HttpStatus.NOT_FOUND, "User not found with id: " + userId);
        }

        if (currentPassword == null || currentPassword.isEmpty()) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "Current password cannot be null or empty");
        }

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "Current password is incorrect");
        }

        if (newPassword == null || newPassword.length() < 6) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "New password must be at least 6 characters long");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        return jwtUtils.generateJwtTokenFromUsername(user.getUsername());
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
    public boolean checkByPhoneAndIdNot(String phone, Long userId) {
        return userRepository.existsByPhoneAndIdNot(phone, userId);
    }

    @Override
    public boolean checkByEmailAndIdNot(String email, Long userId) {
        return userRepository.existsByEmailAndIdNot(email, userId);
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
