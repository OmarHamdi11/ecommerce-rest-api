package com.example.ecommerce_rest_api.features.admin.dto;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DashboardStatsDTO {
    private Long totalProducts;
    private Long totalUsers;
    private Long totalOrders;
    private Long pendingOrders;
    private Long processingOrders;
    private Long deliveredOrders;
    private Double totalRevenue;
    private Long todayOrders;
    private Double todayRevenue;
    private Long lowStockProducts;
    private Long pendingReviews;
}
