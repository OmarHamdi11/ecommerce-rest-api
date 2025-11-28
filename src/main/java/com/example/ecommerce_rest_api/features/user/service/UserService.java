package com.example.ecommerce_rest_api.features.user.service;

import com.example.ecommerce_rest_api.features.user.DTO.AddressDTO;
import com.example.ecommerce_rest_api.features.user.DTO.UserDTO;
import com.example.ecommerce_rest_api.features.user.DTO.UserUpdateRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {

    UserDTO getUserById(Long userId);

    UserDTO updateUser(Long userId, UserUpdateRequest request, MultipartFile profileImage);

    UserDTO updateProfileImage(Long userId, MultipartFile newImage);


    //============= User Address CRUD Operations =============
    AddressDTO addAddress(Long userId, AddressDTO addressDTO);

    List<AddressDTO> getAddressByUserId(Long userId);

    AddressDTO updateAddress(Long userId,Long addressId, AddressDTO addressDTO);

    String deleteAddress(Long userId,Long addressId);
}