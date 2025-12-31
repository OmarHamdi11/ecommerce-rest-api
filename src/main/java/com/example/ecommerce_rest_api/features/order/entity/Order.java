package com.example.ecommerce_rest_api.features.order.entity;

import com.example.ecommerce_rest_api.features.order.ENUM.OrderStatus;
import com.example.ecommerce_rest_api.features.order.ENUM.PaymentMethod;
import com.example.ecommerce_rest_api.features.order.ENUM.PaymentStatus;
import com.example.ecommerce_rest_api.features.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "orders",
        indexes = {
                @Index(name = "idx_order_user", columnList = "user_id"),
                @Index(name = "idx_order_number", columnList = "order_number", unique = true),
                @Index(name = "idx_order_status", columnList = "status")
        }
)
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_number", nullable = false, unique = true)
    private String orderNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Builder.Default
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", nullable = false)
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", nullable = false)
    private PaymentStatus paymentStatus;

    @Column(name = "subtotal", nullable = false)
    private Double subtotal;

    @Column(name = "shipping_cost")
    private Double shippingCost = 0.0;

    @Column(name = "tax")
    private Double tax = 0.0;

    @Column(name = "discount")
    private Double discount = 0.0;

    @Column(name = "total", nullable = false)
    private Double total;

    // Shipping Address
    @Column(name = "shipping_name")
    private String shippingName;

    @Column(name = "shipping_phone")
    private String shippingPhone;

    @Column(name = "shipping_address_line1")
    private String shippingAddressLine1;

    @Column(name = "shipping_address_line2")
    private String shippingAddressLine2;

    @Column(name = "shipping_city")
    private String shippingCity;

    @Column(name = "shipping_country")
    private String shippingCountry;

    @Column(name = "shipping_postal_code")
    private String shippingPostalCode;

    @Column(name = "notes", length = 500)
    private String notes;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "delivered_at")
    private LocalDateTime deliveredAt;

    public void addItem(OrderItem item) {
        items.add(item);
        item.setOrder(this);
    }

    public void calculateTotal() {
        this.subtotal = items.stream()
                .mapToDouble(OrderItem::getSubtotal)
                .sum();
        this.total = subtotal + shippingCost + tax - discount;
    }

}
