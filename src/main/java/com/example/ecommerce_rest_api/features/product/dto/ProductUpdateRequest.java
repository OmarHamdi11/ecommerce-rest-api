package com.example.ecommerce_rest_api.features.product.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductUpdateRequest {
    @NotBlank(message = "Product name is required")
    @Size(min = 3, max = 255, message = "Product name must be between 3 and 255 character")
    private String name;

    @NotBlank(message = "Product description is required")
    @Size(min = 10, message = "Product description must be at least 10 characters")
    private String description;

    @Size(min = 500, message = "Product summery must not exceed 500 character5")
    private String summary;

    @Size(min = 100, message = "Product brand must not exceed 100 character5")
    private String brand;

    private boolean isActive;
    private boolean isFeatured;

    private List<Long> subCategoryIds;
}
