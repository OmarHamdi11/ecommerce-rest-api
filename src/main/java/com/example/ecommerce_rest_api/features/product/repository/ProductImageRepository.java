package com.example.ecommerce_rest_api.features.product.repository;

import com.example.ecommerce_rest_api.features.product.entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductImageRepository  extends JpaRepository<ProductImage,Long> {

    List<ProductImage> findByProductIdOrderByDisplayOrderAsc(Long productId);

    @Query("select pi from ProductImage pi where pi.product.id = :productId and pi.isPrimary = true ")
    Optional<ProductImage> findPrimaryImageByProductId(@Param("productId") Long productId);

    void deleteByProductId(Long productId);

}
