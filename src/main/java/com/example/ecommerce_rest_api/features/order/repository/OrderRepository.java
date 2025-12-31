package com.example.ecommerce_rest_api.features.order.repository;

import com.example.ecommerce_rest_api.features.order.ENUM.OrderStatus;
import com.example.ecommerce_rest_api.features.order.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Page<Order> findByUserId(Long userId, Pageable pageable);

    Page<Order> findByStatus(OrderStatus status, Pageable pageable);

    @Query("SELECT o FROM Order o WHERE o.createdAt >= :startDate AND o.createdAt <= :endDate")
    List<Order> findOrdersInDateRange(@Param("startDate") LocalDateTime startDate,
                                      @Param("endDate") LocalDateTime endDate);

    @Query("SELECT COUNT(o) FROM Order o WHERE o.status = :status")
    Long countByStatus(@Param("status") OrderStatus status);

    @Query("SELECT SUM(o.total) FROM Order o WHERE o.status = :status")
    Double sumTotalByStatus(@Param("status") OrderStatus status);
}