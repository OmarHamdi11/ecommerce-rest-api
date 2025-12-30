package com.example.ecommerce_rest_api.features.cart.repository;

import com.example.ecommerce_rest_api.features.cart.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem,Long> {
}
