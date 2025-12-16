package com.example.ecommerce_rest_api.features.product.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductCreateRequest {

    @NotBlank(message = "Product name is required")
    @Size(min = 3, max = 255, message = "Product name must be between 3 and 255 character")
    private String name;

    @NotBlank(message = "Product description is required")
    @Size(min = 10, message = "Product description must be at least 10 characters")
    private String description;

    @Size(max = 500, message = "Product summery must not exceed 500 characters")
    private String summary;

    @Size(max = 100, message = "Product brand must not exceed 100 characters")
    private String brand;

    private Boolean isActive=true;
    private Boolean isFeatured=false;

    @NotEmpty(message = "At least one category is required")
    private List<Long> subCategoryIds;

    @NotEmpty(message = "At least one SKU is required")
    private List<ProductSkuCreateRequest> skus = new ArrayList<>();
}
