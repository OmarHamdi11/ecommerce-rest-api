package com.example.ecommerce_rest_api.features.product.dto;

import com.example.ecommerce_rest_api.features.product.ENUM.AttributeType;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductAttributeCreateRequest {
    @NotNull(message = "Attribute type is required")
    private AttributeType type;

    @NotBlank(message = "Attribute value is required")
    @Size(max = 50, message = "Value must not exceed 50 characters")
    private String value;

    @Size(max = 50, message = "Display value must not exceed 50 characters")
    private String displayValue;

    @Pattern(regexp = "^#[0-9A-Fa-f]{6}$", message = "Invalid hex color code")
    private String hexCode; // For colors only
}
