package com.example.ecommerce_rest_api.features.cart.dto;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDTO {
    private Long id;
    private Long productId;
    private String productName;
    private String productImage;
    private Long skuId;
    private String skuCode;
    private Integer quantity;
    private Double price;
    private Double currentPrice;
    private Double subtotal;
    private Boolean inStock;
    private Integer availableStock;
}
