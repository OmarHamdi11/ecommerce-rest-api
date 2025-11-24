package com.example.ecommerce_rest_api.features.user.controller;

import com.example.ecommerce_rest_api.common.response.ApiResponse;
import com.example.ecommerce_rest_api.features.user.DTO.UserDTO;
import com.example.ecommerce_rest_api.features.user.DTO.UserUpdateRequest;
import com.example.ecommerce_rest_api.features.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;


@RestController
@RequestMapping("/api/v1/user")
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDTO>> getUserById(@PathVariable(name = "id") Long userId) {
        UserDTO user = userService.getUserById(userId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success("User retrieved successfully",user));

    }

    @PostMapping(value = "/{id}/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<UserDTO>> updateUser(
            @PathVariable(name = "id") Long userid,
            @Valid @RequestPart(name = "data") UserUpdateRequest request,
            @RequestPart(value = "profileImage", required = false) MultipartFile profileImage
    ) {
        UserDTO response = userService.updateUser(userid, request, profileImage);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success("User updated successfully",response));
    }

    @PutMapping("/{id}/profile-image")
    public ResponseEntity<ApiResponse<Map<String,String>>> updateProfileImage(
            @PathVariable Long id,
            @RequestParam("image") MultipartFile image
    ) {
        UserDTO response = userService.updateProfileImage(id, image);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(
                        "Profile image updated successfully",
                        Map.of("profileImageUrl",response.getProfileImageUrl())
                ));
    }

}