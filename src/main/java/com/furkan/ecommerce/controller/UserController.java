package com.furkan.ecommerce.controller;

import com.furkan.ecommerce.config.service.UserDetailsImpl;
import com.furkan.ecommerce.dto.UserDetailDTO;
import com.furkan.ecommerce.payload.request.ChangePasswordRequest;
import com.furkan.ecommerce.payload.response.JwtResponse;
import com.furkan.ecommerce.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${apiPrefix}/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public Page<UserDetailDTO> getAllUsers(Pageable pageable) {
        return userService.getAllUsers(pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDetailDTO> getUserById(@PathVariable("id") Long userId) {
        UserDetailDTO userDetailDTO = userService.getUserById(userId);
        return ResponseEntity.ok(userDetailDTO);
    }

    @PutMapping
    public ResponseEntity<UserDetailDTO> updateProduct(@Valid @AuthenticationPrincipal UserDetailsImpl userDetails,
                                                       @RequestBody UserDetailDTO userDetailDTO) {
        Long id = userDetails.getId();
        UserDetailDTO updatedUser = userService.updateUser(id, userDetailDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(updatedUser);
    }

    @PutMapping("/change-password")
    public ResponseEntity<JwtResponse> changePassword(@Valid @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
                                                 @RequestBody ChangePasswordRequest changePasswordRequest) {
        Long userId = userDetailsImpl.getId();
        String newToken = userService.changePassword(userId, changePasswordRequest.getCurrentPassword(), changePasswordRequest.getNewPassword());
        return ResponseEntity.ok(new JwtResponse(newToken));
    }
}
