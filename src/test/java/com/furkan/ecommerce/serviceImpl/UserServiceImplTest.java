package com.furkan.ecommerce.serviceImpl;

import com.furkan.ecommerce.config.security.jwt.JwtUtils;
import com.furkan.ecommerce.dto.UserDetailDTO;
import com.furkan.ecommerce.exception.CustomException;
import com.furkan.ecommerce.mapper.UserMapper;
import com.furkan.ecommerce.model.User;
import com.furkan.ecommerce.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtils jwtUtils;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllUsers() {
        List<User> users = new ArrayList<>();
        users.add(new User());
        Page<User> page = new PageImpl<>(users);

        Pageable pageable = PageRequest.of(0, 10);
        when(userRepository.findByIsDeletedFalse(pageable)).thenReturn(page);
        when(userMapper.toUserDetailDTO(any(User.class))).thenReturn(new UserDetailDTO());

        Page<UserDetailDTO> result = userService.getAllUsers(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void testGetUserById() {
        User user = new User();
        user.setId(1L);
        when(userRepository.findByIdAndIsDeletedFalse(1L)).thenReturn(user);
        when(userMapper.toUserDetailDTO(user)).thenReturn(new UserDetailDTO());

        UserDetailDTO result = userService.getUserById(1L);

        assertNotNull(result);
    }

    @Test
    void testGetUserByIdNotFound() {
        when(userRepository.findByIdAndIsDeletedFalse(1L)).thenReturn(null);

        CustomException exception = assertThrows(CustomException.class, () -> userService.getUserById(1L));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    void testUpdateUser() {
        User user = new User();
        user.setId(1L);
        user.setEmail("old.email@example.com");
        when(userRepository.findByIdAndIsDeletedFalse(1L)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toUserDetailDTO(user)).thenReturn(new UserDetailDTO());

        UserDetailDTO updatedUserDTO = new UserDetailDTO();
        updatedUserDTO.setEmail("new.email@example.com");

        UserDetailDTO result = userService.updateUser(1L, updatedUserDTO);

        assertNotNull(result);
    }

    @Test
    void testUpdateUserNotFound() {
        when(userRepository.findByIdAndIsDeletedFalse(1L)).thenReturn(null);

        UserDetailDTO updatedUserDTO = new UserDetailDTO();
        CustomException exception = assertThrows(CustomException.class, () -> userService.updateUser(1L, updatedUserDTO));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    void testChangePassword() {
        User user = new User();
        user.setId(1L);
        user.setPassword("encodedCurrentPassword");
        user.setUsername("testUser");

        when(userRepository.findByIdAndIsDeletedFalse(1L)).thenReturn(user);
        when(passwordEncoder.matches("currentPassword", user.getPassword())).thenReturn(true);
        when(passwordEncoder.encode("newPassword")).thenReturn("encodedNewPassword");
        when(jwtUtils.generateJwtTokenFromUsername(user.getUsername())).thenReturn("jwtToken");

        String result = userService.changePassword(1L, "currentPassword", "newPassword");

        assertNotNull(result);
        assertEquals("jwtToken", result);
    }

    @Test
    void testChangePasswordUserNotFound() {
        when(userRepository.findByIdAndIsDeletedFalse(1L)).thenReturn(null);

        CustomException exception = assertThrows(CustomException.class, () -> userService.changePassword(1L, "currentPassword", "newPassword"));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    void testCheckByUsername() {
        when(userRepository.existsByUsername("username")).thenReturn(true);

        Boolean result = userService.checkByUsername("username");

        assertTrue(result);
    }

    @Test
    void testCheckByEmail() {
        when(userRepository.existsByEmail("email@example.com")).thenReturn(true);

        Boolean result = userService.checkByEmail("email@example.com");

        assertTrue(result);
    }

    @Test
    void testCheckByPhoneAndIdNot() {
        when(userRepository.existsByPhoneAndIdNot("1234567890", 1L)).thenReturn(true);

        boolean result = userService.checkByPhoneAndIdNot("1234567890", 1L);

        assertTrue(result);
    }

    @Test
    void testCheckByEmailAndIdNot() {
        when(userRepository.existsByEmailAndIdNot("email@example.com", 1L)).thenReturn(true);

        boolean result = userService.checkByEmailAndIdNot("email@example.com", 1L);

        assertTrue(result);
    }

    @Test
    void testSaveUser() {
        User user = new User();
        when(userRepository.save(user)).thenReturn(user);

        userService.saveUser(user);

        verify(userRepository, times(1)).save(user);
    }
}
