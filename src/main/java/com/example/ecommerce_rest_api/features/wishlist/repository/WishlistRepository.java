package com.example.ecommerce_rest_api.features.wishlist.repository;

import com.example.ecommerce_rest_api.features.wishlist.entity.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface WishlistRepository extends JpaRepository<Wishlist, Long> {
    Optional<Wishlist> findByUserId(Long userId);
}