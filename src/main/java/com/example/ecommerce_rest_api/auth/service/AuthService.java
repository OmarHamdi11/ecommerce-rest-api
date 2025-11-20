package com.example.ecommerce_rest_api.auth.service;

import com.example.ecommerce_rest_api.auth.DTO.RegisterDTO;

public interface AuthService {

    String register(RegisterDTO registerDTO);

}
