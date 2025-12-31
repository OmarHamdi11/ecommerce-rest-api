package com.example.ecommerce_rest_api.features.order.controller;

import com.example.ecommerce_rest_api.common.response.PageResponse;
import com.example.ecommerce_rest_api.common.response.ResponseApi;
import com.example.ecommerce_rest_api.features.order.dto.OrderCreateRequest;
import com.example.ecommerce_rest_api.features.order.dto.OrderDTO;
import com.example.ecommerce_rest_api.features.order.dto.OrderStatusUpdateRequest;
import com.example.ecommerce_rest_api.features.order.service.OrderService;
import com.example.ecommerce_rest_api.utils.AppConstants;
import com.example.ecommerce_rest_api.utils.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/orders")
@Tag(name = "Order Management", description = "APIs for managing orders")
@SecurityRequirement(name = "Bearer Authentication")
public class OrderController {

    private final OrderService orderService;
    private final SecurityUtils securityUtils;

    public OrderController(OrderService orderService, SecurityUtils securityUtils) {
        this.orderService = orderService;
        this.securityUtils = securityUtils;
    }

    @Operation(summary = "Create order", description = "Create new order from cart. User only.")
    @PreAuthorize("hasRole('USER')")
    @PostMapping
    public ResponseEntity<ResponseApi<OrderDTO>> createOrder(
            @Valid @RequestBody OrderCreateRequest request
    ) {
        Long userId = securityUtils.getCurrentUserId();
        OrderDTO order = orderService.createOrder(userId, request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ResponseApi.created("Order created successfully", order));
    }

    @Operation(summary = "Get order by ID", description = "Get order details by ID")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/{orderId}")
    public ResponseEntity<ResponseApi<OrderDTO>> getOrderById(@PathVariable Long orderId) {
        Long userId = securityUtils.getCurrentUserId();
        String role = securityUtils.getCurrentUser().getRole().name();

        OrderDTO order = orderService.getOrderById(orderId, userId, role);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseApi.success("Order retrieved successfully", order));
    }

    @Operation(summary = "Get user orders", description = "Get all orders for current user")
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/my-orders")
    public ResponseEntity<ResponseApi<PageResponse<OrderDTO>>> getUserOrders(
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "createdAt", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "DESC", required = false) String sortDir
    ) {
        Long userId = securityUtils.getCurrentUserId();
        PageResponse<OrderDTO> orders = orderService.getUserOrders(userId, pageNo, pageSize, sortBy, sortDir);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseApi.success("Orders retrieved successfully", orders));
    }

    @Operation(summary = "Get all orders", description = "Get all orders. Admin only.")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<ResponseApi<PageResponse<OrderDTO>>> getAllOrders(
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "createdAt", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "DESC", required = false) String sortDir,
            @RequestParam(value = "status", required = false) String status
    ) {
        PageResponse<OrderDTO> orders = orderService.getAllOrders(pageNo, pageSize, sortBy, sortDir, status);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseApi.success("Orders retrieved successfully", orders));
    }

    @Operation(summary = "Update order status", description = "Update order status. Admin only.")
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{orderId}/status")
    public ResponseEntity<ResponseApi<OrderDTO>> updateOrderStatus(
            @PathVariable Long orderId,
            @Valid @RequestBody OrderStatusUpdateRequest request
    ) {
        OrderDTO order = orderService.updateOrderStatus(orderId, request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseApi.success("Order status updated successfully", order));
    }

    @Operation(summary = "Cancel order", description = "Cancel order. User can cancel own pending orders.")
    @PreAuthorize("hasRole('USER')")
    @PatchMapping("/{orderId}/cancel")
    public ResponseEntity<ResponseApi<OrderDTO>> cancelOrder(@PathVariable Long orderId) {
        Long userId = securityUtils.getCurrentUserId();
        OrderDTO order = orderService.cancelOrder(orderId, userId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseApi.success("Order cancelled successfully", order));
    }
}
