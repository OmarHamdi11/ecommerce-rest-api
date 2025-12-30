package com.example.ecommerce_rest_api.features.cart.entity;

import com.example.ecommerce_rest_api.features.product.entity.ProductSku;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "cart_items",
        indexes = {
                @Index(name = "idx_cart_item_cart", columnList = "cart_id"),
                @Index(name = "idx_cart_item_sku", columnList = "sku_id")
        }
)
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sku_id", nullable = false)
    private ProductSku productSku;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private Double price; // Price at the time of adding to cart

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Double getSubtotal() {
        return price * quantity;
    }
}
