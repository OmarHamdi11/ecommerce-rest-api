package com.example.ecommerce_rest_api.features.user.controller;

import com.example.ecommerce_rest_api.common.response.ApiResponse;
import com.example.ecommerce_rest_api.features.user.DTO.UserDTO;
import com.example.ecommerce_rest_api.features.user.DTO.UserUpdateRequest;
import com.example.ecommerce_rest_api.features.user.service.UserService;
import com.example.ecommerce_rest_api.utils.SecurityUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;


@RestController
@RequestMapping("/api/v1/user")
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService userService;
    private final SecurityUtils securityUtils;

    @Autowired
    public UserController(UserService userService,SecurityUtils securityUtils) {
        this.userService = userService;
        this.securityUtils = securityUtils;
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping
    public ResponseEntity<ApiResponse<UserDTO>> getUserById() {
        Long userId = securityUtils.getCurrentUserId();
        UserDTO user = userService.getUserById(userId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success("User retrieved successfully",user));

    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping(value = "/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<UserDTO>> updateUser(
            @Valid @RequestPart(name = "data") UserUpdateRequest request,
            @RequestPart(value = "profileImage", required = false) MultipartFile profileImage
    ) {
        Long userId = securityUtils.getCurrentUserId();
        UserDTO response = userService.updateUser(userId, request, profileImage);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success("User updated successfully",response));
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping(value = "/profile-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<Map<String,String>>> updateProfileImage(
            @RequestParam("image") MultipartFile image
    ) {
        Long userId = securityUtils.getCurrentUserId();
        UserDTO response = userService.updateProfileImage(userId, image);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(
                        "Profile image updated successfully",
                        Map.of("profileImageUrl",response.getProfileImageUrl())
                ));
    }

}