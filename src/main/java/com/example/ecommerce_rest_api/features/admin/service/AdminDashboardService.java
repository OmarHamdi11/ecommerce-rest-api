package com.example.ecommerce_rest_api.features.admin.service;

import com.example.ecommerce_rest_api.features.admin.dto.*;
import java.time.LocalDate;
import java.util.List;

public interface AdminDashboardService {
    DashboardStatsDTO getDashboardStats();
    SalesReportDTO getSalesReport(LocalDate startDate, LocalDate endDate);
    RecentActivityDTO getRecentActivities(int limit);
    List<LowStockProductDTO> getLowStockProducts();
    List<TopProductDTO> getTopSellingProducts(int limit);
}
