package com.example.ecommerce_rest_api.features.review.controller;

import com.example.ecommerce_rest_api.common.response.PageResponse;
import com.example.ecommerce_rest_api.common.response.ResponseApi;
import com.example.ecommerce_rest_api.features.review.dto.ReviewCreateRequest;
import com.example.ecommerce_rest_api.features.review.dto.ReviewDTO;
import com.example.ecommerce_rest_api.features.review.service.ReviewService;
import com.example.ecommerce_rest_api.utils.AppConstants;
import com.example.ecommerce_rest_api.utils.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/products")
@Tag(name = "Review Management", description = "APIs for managing Product Reviews")
public class ReviewController {

    private final ReviewService reviewService;
    private final SecurityUtils securityUtils;

    public ReviewController(ReviewService reviewService, SecurityUtils securityUtils) {
        this.reviewService = reviewService;
        this.securityUtils = securityUtils;
    }

    @Operation(summary = "Add review", description = "Add review to product. Authenticated users only.")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/reviews")
    public ResponseEntity<ResponseApi<ReviewDTO>> addReview(
            @Valid @RequestBody ReviewCreateRequest request
    ) {
        Long userId = securityUtils.getCurrentUserId();
        ReviewDTO review = reviewService.addReview(userId, request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ResponseApi.created("Review added successfully", review));
    }

    @Operation(summary = "Get product reviews", description = "Get approved reviews for product")
    @GetMapping("/{productId}/reviews")
    public ResponseEntity<ResponseApi<PageResponse<ReviewDTO>>> getProductReviews(
            @PathVariable Long productId,
            @RequestParam(value = "pageNo" , defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize" , defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ) {
        PageResponse<ReviewDTO> reviews = reviewService.getProductReviews(productId, pageNo,pageSize,sortBy,sortDir);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseApi.success("Reviews retrieved", reviews));
    }

    @Operation(summary = "Approve review", description = "Approve product review. Admin only.")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/reviews/{reviewId}/approve")
    public ResponseEntity<ResponseApi<String>> approveReview(@PathVariable Long reviewId) {
        reviewService.approveReview(reviewId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseApi.success("Review approved", null));
    }

    @Operation(summary = "Delete review", description = "Delete product review. Admin only.")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/reviews/{reviewId}")
    public ResponseEntity<ResponseApi<String>> deleteReview(@PathVariable Long reviewId) {
        reviewService.deleteReview(reviewId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseApi.success("Review deleted", null));
    }
}
