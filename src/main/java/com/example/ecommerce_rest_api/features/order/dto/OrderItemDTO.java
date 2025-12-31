package com.example.ecommerce_rest_api.features.order.dto;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDTO {
    private Long id;
    private String productName;
    private String skuCode;
    private Integer quantity;
    private Double price;
    private Double subtotal;
}
