package com.example.ecommerce_rest_api.features.product.dto;

import com.example.ecommerce_rest_api.features.product.ENUM.AttributeType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductAttributeDTO {
    private Long id;
    private AttributeType type;
    private String value;
    private String displayValue;
    private String hexCode;
}
