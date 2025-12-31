package com.example.ecommerce_rest_api.features.order.service;

import com.example.ecommerce_rest_api.common.exception.ResourceNotFoundException;
import com.example.ecommerce_rest_api.common.response.PageResponse;
import com.example.ecommerce_rest_api.features.cart.entity.Cart;
import com.example.ecommerce_rest_api.features.cart.entity.CartItem;
import com.example.ecommerce_rest_api.features.cart.repository.CartRepository;
import com.example.ecommerce_rest_api.features.order.ENUM.OrderStatus;
import com.example.ecommerce_rest_api.features.order.ENUM.PaymentStatus;
import com.example.ecommerce_rest_api.features.order.dto.*;
import com.example.ecommerce_rest_api.features.order.entity.Order;
import com.example.ecommerce_rest_api.features.order.entity.OrderItem;
import com.example.ecommerce_rest_api.features.order.repository.OrderRepository;
import com.example.ecommerce_rest_api.features.product.entity.ProductSku;
import com.example.ecommerce_rest_api.features.product.repository.ProductSkuRepository;
import com.example.ecommerce_rest_api.features.user.entity.User;
import com.example.ecommerce_rest_api.features.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final ProductSkuRepository productSkuRepository;

    public OrderServiceImpl(OrderRepository orderRepository,
                            UserRepository userRepository,
                            CartRepository cartRepository,
                            ProductSkuRepository productSkuRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
        this.productSkuRepository = productSkuRepository;
    }

    @Override
    @Transactional
    public OrderDTO createOrder(Long userId, OrderCreateRequest request) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("User", "id", userId)
        );

        Cart cart = cartRepository.findByUserId(userId).orElseThrow(
                () -> new RuntimeException("Cart is empty")
        );

        if (cart.getItems().isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        // Validate stock availability
        for (CartItem cartItem : cart.getItems()) {
            ProductSku sku = cartItem.getProductSku();
            if (sku.getQuantity() < cartItem.getQuantity()) {
                throw new RuntimeException("Insufficient stock for: " + sku.getProduct().getName());
            }
        }

        // Create order
        Order order = Order.builder()
                .orderNumber(generateOrderNumber())
                .user(user)
                .status(OrderStatus.PENDING)
                .paymentMethod(request.getPaymentMethod())
                .paymentStatus(PaymentStatus.PENDING)
                .shippingCost(request.getShippingCost() != null ? request.getShippingCost() : 0.0)
                .tax(request.getTax() != null ? request.getTax() : 0.0)
                .discount(request.getDiscount() != null ? request.getDiscount() : 0.0)
                .shippingName(request.getShippingName())
                .shippingPhone(request.getShippingPhone())
                .shippingAddressLine1(request.getShippingAddressLine1())
                .shippingAddressLine2(request.getShippingAddressLine2())
                .shippingCity(request.getShippingCity())
                .shippingCountry(request.getShippingCountry())
                .shippingPostalCode(request.getShippingPostalCode())
                .notes(request.getNotes())
                .build();

        // Create order items from cart
        for (CartItem cartItem : cart.getItems()) {
            ProductSku sku = cartItem.getProductSku();

            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .productSku(sku)
                    .productName(sku.getProduct().getName())
                    .skuCode(sku.getSku())
                    .quantity(cartItem.getQuantity())
                    .price(cartItem.getPrice())
                    .build();

            orderItem.calculateSubtotal();
            order.addItem(orderItem);

            // Update stock
            sku.setQuantity(sku.getQuantity() - cartItem.getQuantity());
            productSkuRepository.save(sku);
        }

        order.calculateTotal();
        Order savedOrder = orderRepository.save(order);

        // Clear cart after successful order
        cart.getItems().clear();
        cartRepository.save(cart);

        return mapToOrderDTO(savedOrder);
    }

    @Override
    public OrderDTO getOrderById(Long orderId, Long userId, String role) {
        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new ResourceNotFoundException("Order", "id", orderId)
        );
        // Users can only see their own orders
        if (role.equals("USER") && !order.getUser().getId().equals(userId)) {
            throw new RuntimeException("Access denied");
        }
        return mapToOrderDTO(order);
    }

    @Override
    public PageResponse<OrderDTO> getUserOrders(Long userId, int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Order> orders = orderRepository.findByUserId(userId, pageable);

        return new PageResponse<>(orders.map(this::mapToOrderDTO));
    }

    @Override
    public PageResponse<OrderDTO> getAllOrders(int pageNo, int pageSize, String sortBy, String sortDir, String status) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<Order> orders;
        if (status != null && !status.isEmpty()) {
            OrderStatus orderStatus = OrderStatus.valueOf(status.toUpperCase());
            orders = orderRepository.findByStatus(orderStatus, pageable);
        } else {
            orders = orderRepository.findAll(pageable);
        }

        return new PageResponse<>(orders.map(this::mapToOrderDTO));
    }

    @Override
    @Transactional
    public OrderDTO updateOrderStatus(Long orderId, OrderStatusUpdateRequest request) {
        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new ResourceNotFoundException("Order", "id", orderId)
        );

        order.setStatus(request.getStatus());

        if (request.getPaymentStatus() != null) {
            order.setPaymentStatus(request.getPaymentStatus());
        }

        if (request.getStatus() == OrderStatus.DELIVERED) {
            order.setDeliveredAt(LocalDateTime.now());
        }

        Order savedOrder = orderRepository.save(order);
        return mapToOrderDTO(savedOrder);
    }

    @Override
    @Transactional
    public OrderDTO cancelOrder(Long orderId, Long userId) {
        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new ResourceNotFoundException("Order", "id", orderId)
        );

        if (!order.getUser().getId().equals(userId)) {
            throw new RuntimeException("Access denied");
        }

        if (order.getStatus() != OrderStatus.PENDING && order.getStatus() != OrderStatus.CONFIRMED) {
            throw new RuntimeException("Cannot cancel order in current status");
        }

        order.setStatus(OrderStatus.CANCELLED);

        // Restore stock
        for (OrderItem item : order.getItems()) {
            ProductSku sku = item.getProductSku();
            sku.setQuantity(sku.getQuantity() + item.getQuantity());
            productSkuRepository.save(sku);
        }

        Order savedOrder = orderRepository.save(order);
        return mapToOrderDTO(savedOrder);
    }

    // Helper methods
    private String generateOrderNumber() {
        return "ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private OrderDTO mapToOrderDTO(Order order) {
        List<OrderItemDTO> items = order.getItems().stream()
                .map(this::mapToOrderItemDTO)
                .toList();

        return OrderDTO.builder()
                .id(order.getId())
                .orderNumber(order.getOrderNumber())
                .userId(order.getUser().getId())
                .username(order.getUser().getUsername())
                .items(items)
                .status(order.getStatus())
                .paymentMethod(order.getPaymentMethod())
                .paymentStatus(order.getPaymentStatus())
                .subtotal(order.getSubtotal())
                .shippingCost(order.getShippingCost())
                .tax(order.getTax())
                .discount(order.getDiscount())
                .total(order.getTotal())
                .shippingName(order.getShippingName())
                .shippingPhone(order.getShippingPhone())
                .shippingAddressLine1(order.getShippingAddressLine1())
                .shippingAddressLine2(order.getShippingAddressLine2())
                .shippingCity(order.getShippingCity())
                .shippingCountry(order.getShippingCountry())
                .shippingPostalCode(order.getShippingPostalCode())
                .notes(order.getNotes())
                .createdAt(order.getCreatedAt())
                .deliveredAt(order.getDeliveredAt())
                .build();
    }

    private OrderItemDTO mapToOrderItemDTO(OrderItem item) {
        return OrderItemDTO.builder()
                .id(item.getId())
                .productName(item.getProductName())
                .skuCode(item.getSkuCode())
                .quantity(item.getQuantity())
                .price(item.getPrice())
                .subtotal(item.getSubtotal())
                .build();
    }
}
