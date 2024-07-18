package com.furkan.ecommerce.config.service;

import com.furkan.ecommerce.dto.UserDTO;
import com.furkan.ecommerce.mapper.UserMapper;
import com.furkan.ecommerce.model.User;
import com.furkan.ecommerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    UserService userService;

    @Autowired
    private UserMapper userMapper;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDTO userDTO = userService.getUserByUsername(username);
        User user = userMapper.toUser(userDTO);

        return UserDetailsImpl.build(user);
    }
}

