package com.example.ecommerce_rest_api.features.product.entity;


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
        name = "product_images",
        indexes = {
                @Index(name = "idx_product_image_product", columnList = "product_id")
        }
)
public class ProductImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "image_url",nullable = false,length = 500)
    private String imageUrl;

    @Column(name = "image_delete_url",length = 500)
    private String imageDeleteUrl;

    @Column(name = "image_id",length = 100)
    private String imageId;

    @Column(name = "display_order")
    private Integer displayOrder = 0;

    @Column(name = "is_primary")
    private Boolean isPrimary = false;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id",nullable = false)
    private Product product;
}
