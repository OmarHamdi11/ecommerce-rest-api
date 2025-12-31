package com.example.ecommerce_rest_api.features.order.service;

import com.example.ecommerce_rest_api.common.response.PageResponse;
import com.example.ecommerce_rest_api.features.order.dto.OrderCreateRequest;
import com.example.ecommerce_rest_api.features.order.dto.OrderDTO;
import com.example.ecommerce_rest_api.features.order.dto.OrderStatusUpdateRequest;

public interface OrderService {
    OrderDTO createOrder(Long userId, OrderCreateRequest request);
    OrderDTO getOrderById(Long orderId, Long userId, String role);
    PageResponse<OrderDTO> getUserOrders(Long userId, int pageNo, int pageSize, String sortBy, String sortDir);
    PageResponse<OrderDTO> getAllOrders(int pageNo, int pageSize, String sortBy, String sortDir, String status);
    OrderDTO updateOrderStatus(Long orderId, OrderStatusUpdateRequest request);
    OrderDTO cancelOrder(Long orderId, Long userId);
}
