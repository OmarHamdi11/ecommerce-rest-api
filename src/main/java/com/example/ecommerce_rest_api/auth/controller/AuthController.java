package com.example.ecommerce_rest_api.auth.controller;

import com.example.ecommerce_rest_api.auth.DTO.RegisterDTO;
import com.example.ecommerce_rest_api.auth.service.AuthService;
import com.example.ecommerce_rest_api.common.response.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping(value = {"/register","signup"})
    public ResponseEntity<ApiResponse<String>> register(
            @Valid @RequestBody RegisterDTO registerDTO
    ){
        String response = authService.register(registerDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(response,null));
    }
}
