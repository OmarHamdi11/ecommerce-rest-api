package com.example.ecommerce_rest_api.features.category.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubCategoryDTO {
    private Long id;
    private String name;
    private String description;
    private Long categoryId;
    private String categoryName;
    private LocalDateTime createdAt;
}
