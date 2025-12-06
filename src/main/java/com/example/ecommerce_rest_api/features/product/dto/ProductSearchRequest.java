package com.example.ecommerce_rest_api.features.product.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductSearchRequest {
    private String keyword;
    private List<Long> categoryIds;
    private Double minPrice;
    private Double maxPrice;
    private String brand;
    private Boolean isActive = true;
    private Boolean isFeatured;
    private String sortBy = "createdAt"; // createdAt, name, price, popularity
    private String sortDirection = "DESC"; // ASC, DESC
}
