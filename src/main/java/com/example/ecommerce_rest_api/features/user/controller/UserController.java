package com.example.ecommerce_rest_api.features.user.controller;

import com.example.ecommerce_rest_api.common.response.ResponseApi;
import com.example.ecommerce_rest_api.features.user.DTO.AddressDTO;
import com.example.ecommerce_rest_api.features.user.DTO.UserDTO;
import com.example.ecommerce_rest_api.features.user.DTO.UserUpdateRequest;
import com.example.ecommerce_rest_api.features.user.service.UserService;
import com.example.ecommerce_rest_api.utils.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "User Management", description = "APIs for managing Users")
@SecurityRequirement(name = "Bearer Authentication")
public class UserController {

    private final UserService userService;
    private final SecurityUtils securityUtils;

    @Autowired
    public UserController(UserService userService,SecurityUtils securityUtils) {
        this.userService = userService;
        this.securityUtils = securityUtils;
    }

    @Operation(
            summary = "Get current user profile",
            description = "Retrieves the authenticated user's profile information including personal details and profile image. " +
                    "The user ID is automatically extracted from the JWT token."
    )
    @PreAuthorize("hasRole('USER')")
    @GetMapping
    public ResponseEntity<ResponseApi<UserDTO>> getUserById() {
        Long userId = securityUtils.getCurrentUserId();
        UserDTO user = userService.getUserById(userId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseApi.success("User retrieved successfully",user));

    }

    @Operation(
            summary = "Update user profile",
            description = "Updates the authenticated user's profile information. " +
                    "Can update username, email, password, full name, phone, role, gender, and optionally profile image. " +
                    "Requires multipart/form-data with 'data' (JSON) and optional 'profileImage' (file)."
    )
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

    @Operation(
            summary = "Update profile image",
            description = "Updates only the user's profile image. " +
                    "Accepts image files (JPG, PNG, GIF, WebP) up to 5MB. " +
                    "Old image will be replaced with the new one."
    )
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

    @Operation(
            summary = "Add new address",
            description = "Creates a new delivery/billing address for the authenticated user. " +
                    "Users can have multiple addresses. " +
                    "Address includes title, address lines, country, city, postal code, landmark, and phone number."
    )
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

    @Operation(
            summary = "Get all user addresses",
            description = "Retrieves all saved addresses for the authenticated user. " +
                    "Returns a list of addresses including delivery and billing addresses."
    )
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/address")
    public ResponseEntity<ResponseApi<List<AddressDTO>>> getUserAddresses(){
        Long userId = securityUtils.getCurrentUserId();
        List<AddressDTO> response = userService.getAddressByUserId(userId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseApi.success("Address retrieved successfully",response));
    }

    @Operation(
            summary = "Update address",
            description = "Updates an existing address for the authenticated user. " +
                    "User can only update their own addresses. " +
                    "All address fields will be updated with the provided data."
    )
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

    @Operation(
            summary = "Delete address",
            description = "Deletes a specific address for the authenticated user. " +
                    "User can only delete their own addresses. " +
                    "This action is permanent and cannot be undone."
    )
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