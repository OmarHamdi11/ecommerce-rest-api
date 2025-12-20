package com.example.ecommerce_rest_api.features.review.service;

import com.example.ecommerce_rest_api.common.exception.ResourceNotFoundException;
import com.example.ecommerce_rest_api.common.response.PageResponse;
import com.example.ecommerce_rest_api.features.product.entity.Product;
import com.example.ecommerce_rest_api.features.product.repository.ProductRepository;
import com.example.ecommerce_rest_api.features.review.dto.ReviewCreateRequest;
import com.example.ecommerce_rest_api.features.review.dto.ReviewDTO;
import com.example.ecommerce_rest_api.features.review.entity.Review;
import com.example.ecommerce_rest_api.features.review.repository.ReviewRepository;
import com.example.ecommerce_rest_api.features.user.entity.User;
import com.example.ecommerce_rest_api.features.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class ReviewServiceImpl implements ReviewService{

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public ReviewServiceImpl(ReviewRepository reviewRepository,
                             UserRepository userRepository,
                             ProductRepository productRepository
    ) {
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    @Override
    @Transactional
    public ReviewDTO addReview(Long userId, ReviewCreateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        Product product = findProductById(request.getProductId());

        if (reviewRepository.existsByProductIdAndUserId(request.getProductId(), userId)) {
            throw new RuntimeException("You have already reviewed this product");
        }

        Review review = Review.builder()
                .product(product)
                .user(user)
                .rating(request.getRating())
                .title(request.getTitle())
                .comment(request.getComment())
                .isApproved(false) // Needs admin approval
                .build();

        Review savedReview = reviewRepository.save(review);
        return mapToReviewDTO(savedReview);
    }

    @Override
    public PageResponse<ReviewDTO> getProductReviews(Long productId, int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo,pageSize, sort);

        Page<Review> reviews = reviewRepository.findReviewApprovedByProductId(productId, pageable);

        return new PageResponse<>(reviews.map(this::mapToReviewDTO));
    }

    @Override
    @Transactional
    public void approveReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(
                () -> new ResourceNotFoundException("Review", "id", reviewId)
        );
        review.setIsApproved(true);
        reviewRepository.save(review);
    }

    @Override
    @Transactional
    public void deleteReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(
                () -> new ResourceNotFoundException("Review", "id", reviewId)
        );
        reviewRepository.delete(review);
    }

    private Product findProductById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId));
    }

    private ReviewDTO mapToReviewDTO(Review review) {
        return ReviewDTO.builder()
                .id(review.getId())
                .username(review.getUser().getUsername())
                .rating(review.getRating())
                .title(review.getTitle())
                .comment(review.getComment())
                .isVerifiedPurchase(review.getIsVerifiedPurchase())
                .createdAt(review.getCreatedAt())
                .build();
    }
}
