package com.example.ecommerce_rest_api.features.user.service;

import com.example.ecommerce_rest_api.features.user.DTO.UserDTO;
import com.example.ecommerce_rest_api.features.user.DTO.UserUpdateRequest;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {

    UserDTO getUserById(Long userId);

    UserDTO updateUser(Long userId, UserUpdateRequest request, MultipartFile profileImage);

    UserDTO updateProfileImage(Long userId, MultipartFile newImage);

}