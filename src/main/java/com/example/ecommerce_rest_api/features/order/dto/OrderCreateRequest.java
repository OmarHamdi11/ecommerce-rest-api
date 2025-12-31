package com.example.ecommerce_rest_api.features.order.dto;

import com.example.ecommerce_rest_api.features.order.ENUM.PaymentMethod;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreateRequest {
    @NotNull(message = "Payment method is required")
    private PaymentMethod paymentMethod;

    private Double shippingCost;
    private Double tax;
    private Double discount;

    @NotBlank(message = "Shipping name is required")
    private String shippingName;

    @NotBlank(message = "Shipping phone is required")
    private String shippingPhone;

    @NotBlank(message = "Shipping address is required")
    private String shippingAddressLine1;

    private String shippingAddressLine2;

    @NotBlank(message = "Shipping city is required")
    private String shippingCity;

    @NotBlank(message = "Shipping country is required")
    private String shippingCountry;

    @NotBlank(message = "Postal code is required")
    private String shippingPostalCode;

    private String notes;
}
