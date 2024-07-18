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
    private Set<RoleDTO> roles;

}
