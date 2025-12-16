package com.example.ecommerce_rest_api.features.product.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
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
    private Integer reviewCount;
    private LocalDateTime createdAt;
    private List<String> categoryNames;
    private List<ProductImageDTO> images = new ArrayList<>();
    private List<ProductSkuDTO> skus = new ArrayList<>();
}
