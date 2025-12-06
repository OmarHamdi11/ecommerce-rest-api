package com.example.ecommerce_rest_api.features.review.repository;

import com.example.ecommerce_rest_api.features.review.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review,Long> {

    @Query("select r from Review r where r.product.id=:productId and r.isApproved=true order by r.createdAt desc ")
    Page<Review> findReviewApprovedByProductId (@Param("productId") Long productId, Pageable pageable);

    List<Review> findByProductId(Long productId);

    Page<Review> findByUserId(Long userId, Pageable pageable);

    @Query("select count(r) from Review r where r.product.id=:productId and r.isApproved=true ")
    Integer countApprovedByProductId(@Param("productId") Long productId);

    @Query("select avg(r.rating) from Review r where r.product.id=:productId and r.isApproved=true ")
    Double getAverageRatingByProductId(@Param("productId") Long productId);

    boolean existsByProductIdAndUserId(Long productId, Long userId);

}
