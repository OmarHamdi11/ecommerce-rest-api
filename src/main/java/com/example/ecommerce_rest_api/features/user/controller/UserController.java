package com.example.ecommerce_rest_api.features.user.controller;

import com.example.ecommerce_rest_api.common.response.ResponseApi;
import com.example.ecommerce_rest_api.features.user.DTO.AddressDTO;
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

import java.util.List;
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
    public ResponseEntity<ResponseApi<UserDTO>> getUserById() {
        Long userId = securityUtils.getCurrentUserId();
        UserDTO user = userService.getUserById(userId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseApi.success("User retrieved successfully",user));

    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping(value = "/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseApi<UserDTO>> updateUser(
            @Valid @RequestPart(name = "data") UserUpdateRequest request,
            @RequestPart(value = "profileImage", required = false) MultipartFile profileImage
    ) {
        Long userId = securityUtils.getCurrentUserId();
        UserDTO response = userService.updateUser(userId, request, profileImage);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseApi.success("User updated successfully",response));
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping(value = "/profile-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseApi<Map<String,String>>> updateProfileImage(
            @RequestParam("image") MultipartFile image
    ) {
        Long userId = securityUtils.getCurrentUserId();
        UserDTO response = userService.updateProfileImage(userId, image);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseApi.success(
                        "Profile image updated successfully",
                        Map.of("profileImageUrl",response.getProfileImageUrl())
                ));
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/address")
    public ResponseEntity<ResponseApi<AddressDTO>> addAddress(
            @RequestBody AddressDTO addressDTO
    ){
        Long userId = securityUtils.getCurrentUserId();
        AddressDTO response = userService.addAddress(userId,addressDTO);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseApi.success("Address added successfully",response));
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/address")
    public ResponseEntity<ResponseApi<List<AddressDTO>>> getUserAddresses(){
        Long userId = securityUtils.getCurrentUserId();
        List<AddressDTO> response = userService.getAddressByUserId(userId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseApi.success("Address retrieved successfully",response));
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping("/address/{addressId}")
    public ResponseEntity<ResponseApi<AddressDTO>> updateAddress(
            @PathVariable(name = "addressId") Long addressId,
            @RequestBody AddressDTO addressDTO
    ){
        Long userId = securityUtils.getCurrentUserId();
        AddressDTO response = userService.updateAddress(userId,addressId,addressDTO);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseApi.success("Address updated successfully",response));
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/address/{addressId}")
    public ResponseEntity<ResponseApi<String>> deleteAddress(
            @PathVariable(name = "addressId") Long addressId
    ){
        Long userId = securityUtils.getCurrentUserId();
        String response = userService.deleteAddress(userId,addressId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseApi.success(response,null));
    }

}