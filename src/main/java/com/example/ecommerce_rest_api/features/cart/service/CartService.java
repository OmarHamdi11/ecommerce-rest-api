package com.example.ecommerce_rest_api.features.cart.service;

import com.example.ecommerce_rest_api.features.cart.dto.CartDTO;
import com.example.ecommerce_rest_api.features.cart.dto.CartItemRequest;

public interface CartService {
    CartDTO getCartByUserId(Long userId);
    CartDTO addItemToCart(Long userId, CartItemRequest request);
    CartDTO updateCartItemQuantity(Long userId, Long itemId, Integer quantity);
    CartDTO removeItemFromCart(Long userId, Long itemId);
    void clearCart(Long userId);
}
