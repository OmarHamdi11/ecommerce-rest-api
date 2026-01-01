package com.example.ecommerce_rest_api.features.admin.dto;

import lombok.*;
import java.time.LocalDate;
import java.util.Map;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SalesReportDTO {
    private LocalDate startDate;
    private LocalDate endDate;
    private Long totalOrders;
    private Double totalSales;
    private Long completedOrders;
    private Long cancelledOrders;
    private Double averageOrderValue;
    private Map<LocalDate, Double> dailySales;
}
