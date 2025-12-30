package com.example.ecommerce_rest_api.features.wishlist.service;

import com.example.ecommerce_rest_api.features.wishlist.dto.WishlistDTO;

public interface WishlistService {
    WishlistDTO getWishlistByUserId(Long userId);
    WishlistDTO addItemToWishlist(Long userId, Long productId);
    WishlistDTO removeItemFromWishlist(Long userId, Long productId);
    Boolean isProductInWishlist(Long userId, Long productId);
    void clearWishlist(Long userId);
}
