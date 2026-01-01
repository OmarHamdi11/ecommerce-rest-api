package com.example.ecommerce_rest_api.features.admin.dto;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LowStockProductDTO {
    private Long productId;
    private String productName;
    private String skuCode;
    private Integer currentStock;
    private Integer lowStockThreshold;
}
