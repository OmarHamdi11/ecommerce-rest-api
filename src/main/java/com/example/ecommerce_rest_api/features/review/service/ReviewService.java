package com.example.ecommerce_rest_api.features.review.service;

import com.example.ecommerce_rest_api.common.response.PageResponse;
import com.example.ecommerce_rest_api.features.review.dto.ReviewCreateRequest;
import com.example.ecommerce_rest_api.features.review.dto.ReviewDTO;

public interface ReviewService {
    ReviewDTO addReview(Long userId, ReviewCreateRequest request);
    PageResponse<ReviewDTO> getProductReviews(Long productId, int pageNo, int pageSize, String sortBy, String sortDir);
    void approveReview(Long reviewId);
    void deleteReview(Long reviewId);
}
