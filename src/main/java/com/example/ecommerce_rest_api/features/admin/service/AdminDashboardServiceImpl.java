package com.example.ecommerce_rest_api.features.admin.service;

import com.example.ecommerce_rest_api.features.admin.dto.*;
import com.example.ecommerce_rest_api.features.order.ENUM.OrderStatus;
import com.example.ecommerce_rest_api.features.order.entity.Order;
import com.example.ecommerce_rest_api.features.order.repository.OrderRepository;
import com.example.ecommerce_rest_api.features.product.entity.ProductSku;
import com.example.ecommerce_rest_api.features.product.repository.ProductRepository;
import com.example.ecommerce_rest_api.features.product.repository.ProductSkuRepository;
import com.example.ecommerce_rest_api.features.review.repository.ReviewRepository;
import com.example.ecommerce_rest_api.features.user.repository.UserRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AdminDashboardServiceImpl implements AdminDashboardService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final ProductSkuRepository productSkuRepository;
    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;

    public AdminDashboardServiceImpl(
            OrderRepository orderRepository,
            ProductRepository productRepository,
            ProductSkuRepository productSkuRepository,
            UserRepository userRepository,
            ReviewRepository reviewRepository
    ) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.productSkuRepository = productSkuRepository;
        this.userRepository = userRepository;
        this.reviewRepository = reviewRepository;
    }

    @Override
    public DashboardStatsDTO getDashboardStats() {
        Long totalProducts = productRepository.count();
        Long totalUsers = userRepository.count();
        Long totalOrders = orderRepository.count();

        Long pendingOrders = orderRepository.countByStatus(OrderStatus.PENDING);
        Long processingOrders = orderRepository.countByStatus(OrderStatus.PROCESSING);
        Long deliveredOrders = orderRepository.countByStatus(OrderStatus.DELIVERED);

        Double totalRevenue = orderRepository.sumTotalByStatus(OrderStatus.DELIVERED);
        if (totalRevenue == null) totalRevenue = 0.0;

        // Calculate today's stats
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDate.now().plusDays(1).atStartOfDay();

        List<Order> todayOrders = orderRepository.findOrdersInDateRange(startOfDay, endOfDay);
        Long todayOrdersCount = (long) todayOrders.size();
        Double todayRevenue = todayOrders.stream()
                .filter(o -> o.getStatus() == OrderStatus.DELIVERED)
                .mapToDouble(Order::getTotal)
                .sum();

        // Get low stock count
        List<ProductSku> lowStockSkus = productSkuRepository.findLowStockSkus();
        Long lowStockCount = (long) lowStockSkus.size();

        // Get pending reviews
        Long pendingReviews = reviewRepository.findAll().stream()
                .filter(r -> !r.getIsApproved())
                .count();

        return DashboardStatsDTO.builder()
                .totalProducts(totalProducts)
                .totalUsers(totalUsers)
                .totalOrders(totalOrders)
                .pendingOrders(pendingOrders)
                .processingOrders(processingOrders)
                .deliveredOrders(deliveredOrders)
                .totalRevenue(totalRevenue)
                .todayOrders(todayOrdersCount)
                .todayRevenue(todayRevenue)
                .lowStockProducts(lowStockCount)
                .pendingReviews(pendingReviews)
                .build();
    }

    @Override
    public SalesReportDTO getSalesReport(LocalDate startDate, LocalDate endDate) {
        LocalDateTime start = startDate.atStartOfDay();
        LocalDateTime end = endDate.plusDays(1).atStartOfDay();

        List<Order> orders = orderRepository.findOrdersInDateRange(start, end);

        Long totalOrders = (long) orders.size();
        Double totalSales = orders.stream()
                .filter(o -> o.getStatus() == OrderStatus.DELIVERED)
                .mapToDouble(Order::getTotal)
                .sum();

        Long completedOrders = orders.stream()
                .filter(o -> o.getStatus() == OrderStatus.DELIVERED)
                .count();

        Long cancelledOrders = orders.stream()
                .filter(o -> o.getStatus() == OrderStatus.CANCELLED)
                .count();

        Double averageOrderValue = completedOrders > 0 ? totalSales / completedOrders : 0.0;

        // Group by date for daily sales
        Map<LocalDate, Double> dailySales = orders.stream()
                .filter(o -> o.getStatus() == OrderStatus.DELIVERED)
                .collect(Collectors.groupingBy(
                        o -> o.getCreatedAt().toLocalDate(),
                        Collectors.summingDouble(Order::getTotal)
                ));

        return SalesReportDTO.builder()
                .startDate(startDate)
                .endDate(endDate)
                .totalOrders(totalOrders)
                .totalSales(totalSales)
                .completedOrders(completedOrders)
                .cancelledOrders(cancelledOrders)
                .averageOrderValue(averageOrderValue)
                .dailySales(dailySales)
                .build();
    }

    @Override
    public RecentActivityDTO getRecentActivities(int limit) {
        // Get recent orders
        List<Order> recentOrders = orderRepository.findAll(
                PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "createdAt"))
        ).getContent();

        List<ActivityItemDTO> activities = recentOrders.stream()
                .map(order -> ActivityItemDTO.builder()
                        .type("ORDER")
                        .description("New order " + order.getOrderNumber())
                        .timestamp(order.getCreatedAt())
                        .build())
                .toList();

        return RecentActivityDTO.builder()
                .activities(activities)
                .build();
    }

    @Override
    public List<LowStockProductDTO> getLowStockProducts() {
        List<ProductSku> lowStockSkus = productSkuRepository.findLowStockSkus();

        return lowStockSkus.stream()
                .map(sku -> LowStockProductDTO.builder()
                        .productId(sku.getProduct().getId())
                        .productName(sku.getProduct().getName())
                        .skuCode(sku.getSku())
                        .currentStock(sku.getQuantity())
                        .lowStockThreshold(sku.getLowStockThreshold())
                        .build())
                .toList();
    }

    @Override
    public List<TopProductDTO> getTopSellingProducts(int limit) {
        // This is a simplified version. For production, you'd want to track this in order_items
        List<Order> allOrders = orderRepository.findAll();

        Map<Long, Integer> productSales = new HashMap<>();
        Map<Long, String> productNames = new HashMap<>();
        Map<Long, Double> productRevenue = new HashMap<>();

        allOrders.stream()
                .filter(o -> o.getStatus() == OrderStatus.DELIVERED)
                .flatMap(o -> o.getItems().stream())
                .forEach(item -> {
                    Long productId = item.getProductSku().getProduct().getId();
                    productSales.merge(productId, item.getQuantity(), Integer::sum);
                    productNames.put(productId, item.getProductName());
                    productRevenue.merge(productId, item.getSubtotal(), Double::sum);
                });

        return productSales.entrySet().stream()
                .sorted(Map.Entry.<Long, Integer>comparingByValue().reversed())
                .limit(limit)
                .map(entry -> TopProductDTO.builder()
                        .productId(entry.getKey())
                        .productName(productNames.get(entry.getKey()))
                        .totalSold(entry.getValue())
                        .revenue(productRevenue.get(entry.getKey()))
                        .build())
                .toList();
    }
}
