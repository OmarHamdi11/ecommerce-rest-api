package com.example.ecommerce_rest_api.features.product.repository;

import com.example.ecommerce_rest_api.features.product.entity.ProductSku;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductSkuRepository extends JpaRepository<ProductSku,Long> {

    Optional<ProductSku> findBySku(String sku);

    boolean existsBySku(String sku);

    @Query("select ps from ProductSku ps where ps.product.id = :productId and ps.deletedAt is null ")
    List<ProductSku> findByProductId(@Param("productId") Long productId);

    @Query("select ps from ProductSku ps where ps.product.id = :productId and ps.isActive=true and ps.deletedAt is null ")
    List<ProductSku> findActiveByProductId(@Param("productId") Long productId);

    @Query("select ps from ProductSku ps where ps.quantity <= ps.lowStockThreshold and ps.isActive=true and ps.deletedAt is null ")
    List<ProductSku> findLowStockSkus();

    @Query("select ps from ProductSku ps where ps.quantity = 0 and ps.isActive=true and ps.deletedAt is null ")
    List<ProductSku> findOutOfStock();
}
