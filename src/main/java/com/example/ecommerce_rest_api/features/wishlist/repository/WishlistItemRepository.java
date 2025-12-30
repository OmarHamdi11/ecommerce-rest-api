package com.example.ecommerce_rest_api.features.wishlist.repository;

import com.example.ecommerce_rest_api.features.wishlist.entity.WishlistItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishlistItemRepository extends JpaRepository<WishlistItem, Long> {
}