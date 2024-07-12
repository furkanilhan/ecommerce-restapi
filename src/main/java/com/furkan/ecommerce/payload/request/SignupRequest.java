package com.furkan.ecommerce.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

@Data
public class SignupRequest {
    @NotBlank
    @Size(min = 3, max = 20)
    private String username;

    @NotBlank
    @Size(min = 5, max = 30)
    private String email;

    @NotBlank
    @Size(min = 6, max = 40)
    private String password;

    private Set<String> role;

}
