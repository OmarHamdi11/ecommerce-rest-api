package com.example.ecommerce_rest_api.features.category.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryDTO {
    private Long id;
    private String name;
    private String description;
    private LocalDateTime createdAt;
    private List<SubCategoryDTO> subCategories;
}
