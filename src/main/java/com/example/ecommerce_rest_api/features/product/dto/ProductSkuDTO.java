package com.example.ecommerce_rest_api.features.product.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductSkuDTO {
    private Long id;
    private String sku;
    private Double price;
    private Double compareAtPrice;
    private Double discountPercentage;
    private Integer quantity;
    private Boolean isLowStock;
    private Boolean isOutOfStock;
    private Double weight;
    private Boolean isActive;
    private List<ProductAttributeDTO> attributes;
    private LocalDateTime createdAt;
}
