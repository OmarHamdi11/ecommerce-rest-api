package com.example.ecommerce_rest_api.auth.service;

import com.example.ecommerce_rest_api.auth.DTO.RegisterDTO;
import com.example.ecommerce_rest_api.auth.DTO.LoginResponse;
import com.example.ecommerce_rest_api.user.entity.User;
import com.example.ecommerce_rest_api.user.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthServiceImpl implements AuthService{

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public AuthServiceImpl(UserRepository userRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
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
        user.setPassword(registerDTO.getPassword());
        user.setRole(registerDTO.getRole());
        user.setGender(registerDTO.getGender());
        user.setCreatedAt(LocalDateTime.now());

        userRepository.save(user);

        return "User registered successfully";
    }


}
