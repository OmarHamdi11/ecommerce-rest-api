package com.example.ecommerce_rest_api.features.order.dto;

import com.example.ecommerce_rest_api.features.order.ENUM.OrderStatus;
import com.example.ecommerce_rest_api.features.order.ENUM.PaymentStatus;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderStatusUpdateRequest {
    @NotNull(message = "Order status is required")
    private OrderStatus status;

    private PaymentStatus paymentStatus;
}
