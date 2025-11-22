package com.example.ecommerce_rest_api.features.auth.service;

import com.example.ecommerce_rest_api.features.auth.DTO.LoginDTO;
import com.example.ecommerce_rest_api.features.auth.DTO.LoginResponse;
import com.example.ecommerce_rest_api.features.auth.DTO.RegisterDTO;
import com.example.ecommerce_rest_api.security.JwtTokenProvider;
import com.example.ecommerce_rest_api.features.user.entity.User;
import com.example.ecommerce_rest_api.features.user.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthServiceImpl implements AuthService{

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    @Autowired
    public AuthServiceImpl(UserRepository userRepository,
                           AuthenticationManager authenticationManager,
                           JwtTokenProvider jwtTokenProvider,
                           PasswordEncoder passwordEncoder,
                           ModelMapper modelMapper
    ) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
    }

    @Override
    public String register(RegisterDTO registerDTO) {

        // add check for username exist in db
        if (userRepository.existsByUsername(registerDTO.getUsername())){
            throw new RuntimeException("Username is already exists");
        }

        // add check for username exist in db
        if (userRepository.existsByUsername(registerDTO.getEmail())){
            throw new RuntimeException("Email is already exists");
        }

        if (!registerDTO.getPassword().equals(registerDTO.getConfirmPassword())){
            throw new RuntimeException("Password and Confirm Password do not match");
        }

        User user = new User();
        user.setUsername(registerDTO.getUsername());
        user.setEmail(registerDTO.getEmail());
        user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        user.setRole(registerDTO.getRole());
        user.setGender(registerDTO.getGender());
        user.setCreatedAt(LocalDateTime.now());

        userRepository.save(user);

        return "User registered successfully";
    }

    @Override
    public LoginResponse login(LoginDTO loginDTO) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDTO.getUsername(),loginDTO.getPassword()
        ));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtTokenProvider.generateToken(authentication);
        User user = userRepository.findByUsername(loginDTO.getUsername()).orElseThrow(
                () -> new RuntimeException("User not found")
        );

        LoginResponse response = new LoginResponse();
        response.setAccessToken(token);
        response.setUsername(user.getUsername());
        response.setRole(user.getRole());

        return response;
    }


}
