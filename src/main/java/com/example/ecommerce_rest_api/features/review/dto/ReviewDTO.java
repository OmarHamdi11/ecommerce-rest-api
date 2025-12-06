package com.example.ecommerce_rest_api.features.review.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

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
