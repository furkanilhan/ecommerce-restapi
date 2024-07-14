package com.furkan.ecommerce.controller;

import com.furkan.ecommerce.dto.UserDTO;
import com.furkan.ecommerce.mapper.EntityToDTO;
import com.furkan.ecommerce.model.User;
import com.furkan.ecommerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${apiPrefix}/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private EntityToDTO entityToDTO;


    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable("id") Long userId) {
        User user = userService.getUserById(userId);
        UserDTO userDTO = entityToDTO.toUserDTO(user);
        return ResponseEntity.ok(userDTO);
    }
}
