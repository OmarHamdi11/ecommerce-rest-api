package com.example.ecommerce_rest_api.features.admin.controller;

import com.example.ecommerce_rest_api.common.response.ResponseApi;
import com.example.ecommerce_rest_api.features.admin.dto.DashboardStatsDTO;
import com.example.ecommerce_rest_api.features.admin.dto.RecentActivityDTO;
import com.example.ecommerce_rest_api.features.admin.dto.SalesReportDTO;
import com.example.ecommerce_rest_api.features.admin.service.AdminDashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/admin/dashboard")
@Tag(name = "Admin Dashboard", description = "APIs for admin dashboard statistics and reports")
@SecurityRequirement(name = "Bearer Authentication")
@PreAuthorize("hasRole('ADMIN')")
public class AdminDashboardController {

    private final AdminDashboardService dashboardService;

    public AdminDashboardController(AdminDashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @Operation(summary = "Get dashboard statistics", description = "Get overall statistics for admin dashboard")
    @GetMapping("/stats")
    public ResponseEntity<ResponseApi<DashboardStatsDTO>> getDashboardStats() {
        DashboardStatsDTO stats = dashboardService.getDashboardStats();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseApi.success("Dashboard stats retrieved successfully", stats));
    }

    @Operation(summary = "Get sales report", description = "Get sales report for date range")
    @GetMapping("/sales-report")
    public ResponseEntity<ResponseApi<SalesReportDTO>> getSalesReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        SalesReportDTO report = dashboardService.getSalesReport(startDate, endDate);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseApi.success("Sales report retrieved successfully", report));
    }

    @Operation(summary = "Get recent activities", description = "Get recent system activities")
    @GetMapping("/recent-activities")
    public ResponseEntity<ResponseApi<RecentActivityDTO>> getRecentActivities(
            @RequestParam(defaultValue = "10") int limit
    ) {
        RecentActivityDTO activities = dashboardService.getRecentActivities(limit);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseApi.success("Recent activities retrieved successfully", activities));
    }

    @Operation(summary = "Get low stock products", description = "Get products with low stock")
    @GetMapping("/low-stock-products")
    public ResponseEntity<ResponseApi<?>> getLowStockProducts() {
        var products = dashboardService.getLowStockProducts();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseApi.success("Low stock products retrieved successfully", products));
    }

    @Operation(summary = "Get top selling products", description = "Get top selling products")
    @GetMapping("/top-products")
    public ResponseEntity<ResponseApi<?>> getTopSellingProducts(
            @RequestParam(defaultValue = "10") int limit
    ) {
        var products = dashboardService.getTopSellingProducts(limit);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseApi.success("Top products retrieved successfully", products));
    }
}
