package com.example.ecommerce_rest_api.features.wishlist.controller;

import com.example.ecommerce_rest_api.common.response.ResponseApi;
import com.example.ecommerce_rest_api.features.wishlist.dto.WishlistDTO;
import com.example.ecommerce_rest_api.features.wishlist.service.WishlistService;
import com.example.ecommerce_rest_api.utils.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/wishlist")
@Tag(name = "Wishlist", description = "APIs for managing user wishlist")
@SecurityRequirement(name = "Bearer Authentication")
@PreAuthorize("hasRole('USER')")
public class WishlistController {

    private final WishlistService wishlistService;
    private final SecurityUtils securityUtils;

    public WishlistController(WishlistService wishlistService, SecurityUtils securityUtils) {
        this.wishlistService = wishlistService;
        this.securityUtils = securityUtils;
    }

    @Operation(summary = "Get user wishlist", description = "Get current user's wishlist with all items")
    @GetMapping
    public ResponseEntity<ResponseApi<WishlistDTO>> getWishlist() {
        Long userId = securityUtils.getCurrentUserId();
        WishlistDTO wishlist = wishlistService.getWishlistByUserId(userId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseApi.success("Wishlist retrieved successfully", wishlist));
    }

    @Operation(summary = "Add item to wishlist", description = "Add product to wishlist")
    @PostMapping("/items/{productId}")
    public ResponseEntity<ResponseApi<WishlistDTO>> addItemToWishlist(
            @PathVariable Long productId
    ) {
        Long userId = securityUtils.getCurrentUserId();
        WishlistDTO wishlist = wishlistService.addItemToWishlist(userId, productId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseApi.success("Item added to wishlist successfully", wishlist));
    }

    @Operation(summary = "Remove item from wishlist", description = "Remove product from wishlist")
    @DeleteMapping("/items/{productId}")
    public ResponseEntity<ResponseApi<WishlistDTO>> removeItemFromWishlist(
            @PathVariable Long productId
    ) {
        Long userId = securityUtils.getCurrentUserId();
        WishlistDTO wishlist = wishlistService.removeItemFromWishlist(userId, productId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseApi.success("Item removed from wishlist successfully", wishlist));
    }

    @Operation(summary = "Check if product in wishlist", description = "Check if product exists in user's wishlist")
    @GetMapping("/check/{productId}")
    public ResponseEntity<ResponseApi<Boolean>> isProductInWishlist(@PathVariable Long productId) {
        Long userId = securityUtils.getCurrentUserId();
        Boolean inWishlist = wishlistService.isProductInWishlist(userId, productId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseApi.success("Check completed", inWishlist));
    }

    @Operation(summary = "Clear wishlist", description = "Remove all items from wishlist")
    @DeleteMapping("/clear")
    public ResponseEntity<ResponseApi<String>> clearWishlist() {
        Long userId = securityUtils.getCurrentUserId();
        wishlistService.clearWishlist(userId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseApi.success("Wishlist cleared successfully", null));
    }
}
