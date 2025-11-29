package com.example.ecommerce_rest_api.features.auth.controller;

import com.example.ecommerce_rest_api.features.auth.DTO.LoginDTO;
import com.example.ecommerce_rest_api.features.auth.DTO.LoginResponse;
import com.example.ecommerce_rest_api.features.auth.DTO.RegisterDTO;
import com.example.ecommerce_rest_api.features.auth.service.AuthService;
import com.example.ecommerce_rest_api.common.response.ResponseApi;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Authentication", description = "APIs for user authentication and registration")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    //Build Register REST API
    @Operation(
            summary = "User registration",
            description = "Registers a new user account with username, email, and password. Creates user with default USER role."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User registered successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid registration data or user already exists")
    })
    @PostMapping(value = {"/register","signup"})
    public ResponseEntity<ResponseApi<String>> register(
            @Valid @RequestBody RegisterDTO registerDTO
    ){
        String response = authService.register(registerDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ResponseApi.success(response,null));
    }

    //Build Login REST API
    @Operation(
            summary = "User login",
            description = "Authenticates a user with username/email and password, returns JWT access token upon successful authentication"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful, JWT token returned"),
            @ApiResponse(responseCode = "400", description = "Invalid login credentials provided"),
            @ApiResponse(responseCode = "401", description = "Authentication failed - Invalid username or password")
    })
    @PostMapping(value = {"/login","signin"})
    public ResponseEntity<ResponseApi<LoginResponse>> login(
            @Valid @RequestBody LoginDTO loginDTO
    ){
        LoginResponse response = authService.login(loginDTO);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseApi.success("Logged in Successfully",response));
    }

}
