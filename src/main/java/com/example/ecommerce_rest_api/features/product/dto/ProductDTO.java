package com.example.ecommerce_rest_api.features.product.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    private Long id;
    private String name;
    private String slug;
    private String description;
    private String summary;
    private String coverImage;
    private String brand;
    private boolean isActive;
    private boolean isFeatured;
    private Integer viewCount;
    private Double minPrice;
    private Double maxPrice;
    private Integer totalStock;
    private Double averageRating;
    private LocalDateTime createdAt;
    private List<ProductImageDTO> images = new ArrayList<>();
    private List<ProductSkuDTO> skus = new ArrayList<>();
}
