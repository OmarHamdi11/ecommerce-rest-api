package com.example.ecommerce_rest_api.features.product.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductSkuCreateRequest {
    @NotBlank(message = "SKU code is required")
    @Size(max = 100, message = "SKU code must not exceed 100 characters")
    private String sku;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.01", message = "Price must be greater than 0")
    private Double price;

    @DecimalMin(value = "0.00", message = "Compare price must be positive")
    private Double compareAtPrice;

    @DecimalMin(value = "0.00", message = "Cost price must be positive")
    private Double costPrice;

    @NotNull(message = "Quantity is required")
    @Min(value = 0, message = "Quantity cannot be negative")
    private Integer quantity;

    @Min(value = 1, message = "Low stock threshold must be at least 1")
    private Integer lowStockThreshold = 10;

    @DecimalMin(value = "0.00", message = "Weight must be positive")
    private Double weight;

    private Boolean isActive = true;

    private List<Long> attributeIds;
}
