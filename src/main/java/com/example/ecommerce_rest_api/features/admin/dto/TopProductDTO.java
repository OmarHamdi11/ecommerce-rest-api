package com.example.ecommerce_rest_api.features.admin.dto;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TopProductDTO {
    private Long productId;
    private String productName;
    private Integer totalSold;
    private Double revenue;
}
