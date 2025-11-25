package com.example.ecommerce_rest_api.utils;

import com.example.ecommerce_rest_api.features.user.entity.User;
import com.example.ecommerce_rest_api.features.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtils {

    private final UserRepository userRepository;

    @Autowired
    public SecurityUtils(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getCurrentUser(){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null){
            throw new RuntimeException("No authenticated user found");
        }

        String username = authentication.getName();
        System.out.println(username);

        return userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));

    }

    public String getCurrentUsername(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null ? authentication.getName() : null;
    }

    public Long getCurrentUserId(){
        return getCurrentUser().getId();
    }

}

