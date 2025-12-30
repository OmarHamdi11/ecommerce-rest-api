package com.example.ecommerce_rest_api.features.cart.repository;

import com.example.ecommerce_rest_api.features.cart.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart,Long> {
    Optional<Cart> findByUserId(Long userId);
}
