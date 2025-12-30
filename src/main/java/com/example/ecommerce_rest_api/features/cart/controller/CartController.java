package com.example.ecommerce_rest_api.features.cart.controller;

import com.example.ecommerce_rest_api.common.response.ResponseApi;
import com.example.ecommerce_rest_api.features.cart.dto.CartDTO;
import com.example.ecommerce_rest_api.features.cart.dto.CartItemRequest;
import com.example.ecommerce_rest_api.features.cart.service.CartService;
import com.example.ecommerce_rest_api.utils.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cart")
@Tag(name = "Shopping Cart", description = "APIs for managing shopping cart")
@SecurityRequirement(name = "Bearer Authentication")
@PreAuthorize("hasRole('USER')")
public class CartController {

    private final CartService cartService;
    private final SecurityUtils securityUtils;

    public CartController(CartService cartService, SecurityUtils securityUtils) {
        this.cartService = cartService;
        this.securityUtils = securityUtils;
    }

    @Operation(summary = "Get user cart", description = "Get current user's shopping cart with all items")
    @GetMapping
    public ResponseEntity<ResponseApi<CartDTO>> getCart() {
        Long userId = securityUtils.getCurrentUserId();
        CartDTO cart = cartService.getCartByUserId(userId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseApi.success("Cart retrieved successfully", cart));
    }

    @Operation(summary = "Add item to cart", description = "Add product SKU to cart or update quantity if exists")
    @PostMapping("/items")
    public ResponseEntity<ResponseApi<CartDTO>> addItemToCart(
            @Valid @RequestBody CartItemRequest request
    ) {
        Long userId = securityUtils.getCurrentUserId();
        CartDTO cart = cartService.addItemToCart(userId, request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseApi.success("Item added to cart successfully", cart));
    }

    @Operation(summary = "Update cart item quantity", description = "Update quantity of specific cart item")
    @PutMapping("/items/{itemId}")
    public ResponseEntity<ResponseApi<CartDTO>> updateCartItemQuantity(
            @PathVariable Long itemId,
            @RequestParam Integer quantity
    ) {
        Long userId = securityUtils.getCurrentUserId();
        CartDTO cart = cartService.updateCartItemQuantity(userId, itemId, quantity);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseApi.success("Cart item updated successfully", cart));
    }

    @Operation(summary = "Remove item from cart", description = "Remove specific item from cart")
    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<ResponseApi<CartDTO>> removeItemFromCart(@PathVariable Long itemId) {
        Long userId = securityUtils.getCurrentUserId();
        CartDTO cart = cartService.removeItemFromCart(userId, itemId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseApi.success("Item removed from cart successfully", cart));
    }

    @Operation(summary = "Clear cart", description = "Remove all items from cart")
    @DeleteMapping("/clear")
    public ResponseEntity<ResponseApi<String>> clearCart() {
        Long userId = securityUtils.getCurrentUserId();
        cartService.clearCart(userId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseApi.success("Cart cleared successfully", null));
    }
}
