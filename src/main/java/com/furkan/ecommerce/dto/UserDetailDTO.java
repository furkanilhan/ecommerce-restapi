package com.furkan.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailDTO {

    private Long id;
    private String username;
    private String email;
    private String phone;
    private String firstName;
    private String lastName;
    private boolean enabled;
    private CartDTO cart;
    private List<AddressDTO> addresses;
    private Set<RoleDTO> roles;

}
