package com.example.ecommerce_rest_api.features.review.dto;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDTO {
    private Long id;
    private String username;
    private Integer rating;
    private String title;
    private String comment;
    private Boolean isVerifiedPurchase;
    private LocalDateTime createdAt;
}
