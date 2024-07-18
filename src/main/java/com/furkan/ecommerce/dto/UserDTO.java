package com.furkan.ecommerce.dto;

import lombok.*;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private Long id;
    private String username;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private boolean enabled;
    private CartDTO cart;
    private Set<RoleDTO> roles;

}
