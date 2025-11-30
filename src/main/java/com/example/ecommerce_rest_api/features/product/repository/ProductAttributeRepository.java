package com.example.ecommerce_rest_api.features.product.repository;

import com.example.ecommerce_rest_api.features.product.ENUM.AttributeType;
import com.example.ecommerce_rest_api.features.product.entity.ProductAttribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductAttributeRepository  extends JpaRepository<ProductAttribute,Long> {

    Optional<ProductAttribute> findByTypeAndValue(AttributeType type, String value);

    List<ProductAttribute> findByType(AttributeType type);

    boolean existsByTypeAndValue(AttributeType type, String value);

    @Query("select pa from ProductAttribute pa join pa.skus ps where ps.product.id = :productId")
    List<ProductAttribute> findByProductId(@Param("productId") Long productId);
}
