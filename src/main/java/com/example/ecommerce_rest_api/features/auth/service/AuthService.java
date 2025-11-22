package com.example.ecommerce_rest_api.features.auth.service;

import com.example.ecommerce_rest_api.features.auth.DTO.LoginDTO;
import com.example.ecommerce_rest_api.features.auth.DTO.LoginResponse;
import com.example.ecommerce_rest_api.features.auth.DTO.RegisterDTO;

public interface AuthService {

    String register(RegisterDTO registerDTO);

    LoginResponse login(LoginDTO loginDTO);

}
