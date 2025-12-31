package com.example.ecommerce_rest_api.features.order.dto;

import com.example.ecommerce_rest_api.features.order.ENUM.OrderStatus;
import com.example.ecommerce_rest_api.features.order.ENUM.PaymentMethod;
import com.example.ecommerce_rest_api.features.order.ENUM.PaymentStatus;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    private Long id;
    private String orderNumber;
    private Long userId;
    private String username;
    private List<OrderItemDTO> items;
    private OrderStatus status;
    private PaymentMethod paymentMethod;
    private PaymentStatus paymentStatus;
    private Double subtotal;
    private Double shippingCost;
    private Double tax;
    private Double discount;
    private Double total;
    private String shippingName;
    private String shippingPhone;
    private String shippingAddressLine1;
    private String shippingAddressLine2;
    private String shippingCity;
    private String shippingCountry;
    private String shippingPostalCode;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime deliveredAt;
}
