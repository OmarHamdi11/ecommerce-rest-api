package com.example.ecommerce_rest_api.features.user.DTO;

import com.example.ecommerce_rest_api.features.user.ENUM.Gender;
import com.example.ecommerce_rest_api.features.user.ENUM.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String username;
    private String email;
    private String password;
    private String fullName;
    private String phone;
    private Role role;
    private Gender gender;
    private LocalDateTime createdAt;
}
