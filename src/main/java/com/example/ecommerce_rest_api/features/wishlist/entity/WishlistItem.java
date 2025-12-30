package com.example.ecommerce_rest_api.features.wishlist.entity;


import com.example.ecommerce_rest_api.features.product.entity.Product;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "wishlist_items",
        uniqueConstraints = @UniqueConstraint(columnNames = {"wishlist_id", "product_id"}),
        indexes = {
                @Index(name = "idx_wishlist_item_wishlist", columnList = "wishlist_id"),
                @Index(name = "idx_wishlist_item_product", columnList = "product_id")
        }
)
public class WishlistItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wishlist_id", nullable = false)
    private Wishlist wishlist;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @CreationTimestamp
    @Column(name = "added_at", updatable = false)
    private LocalDateTime addedAt;
}
