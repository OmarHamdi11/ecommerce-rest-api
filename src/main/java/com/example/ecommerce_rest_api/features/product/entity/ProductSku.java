package com.example.ecommerce_rest_api.features.product.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "product_skus",
        indexes = {
                @Index(name = "idx_sku_code", columnList = "sku", unique = true),
                @Index(name = "idx_sku_product",columnList = "product_id"),
                @Index(name = "idx_sku_active", columnList = "is_active")
        }
)
public class ProductSku {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,unique = true,length = 100)
    private String sku;

    @Column(nullable = false)
    private Double price;

    @Column(name = "compare_at_price")
    private Double compareAtPrice;

    @Column(name = "cost_price")
    private Double costPrice;

    @Column(nullable = false)
    private Integer quantity;

    @Column(name = "low_stock_threshold")
    private Integer lowStockThreshold = 10;

    @Column
    private Double weight;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @CreationTimestamp
    @Column(name = "created_at",updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "sku_attributes",
            joinColumns = @JoinColumn(name = "sku_id"),
            inverseJoinColumns = @JoinColumn(name = "attribute_id")
    )
    private List<ProductAttribute> attributes = new ArrayList<>();

    public boolean isLowStock(){
        return quantity <= lowStockThreshold;
    }

    public boolean isOutOfStock(){
        return quantity <= 0;
    }

    public Double getDiscountPercentage(){
        if (compareAtPrice != null && compareAtPrice > price){
            return ((compareAtPrice - price)/ compareAtPrice) * 100 ;
        }
        return  0.0;
    }
}
