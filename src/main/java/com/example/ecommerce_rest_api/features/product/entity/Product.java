package com.example.ecommerce_rest_api.features.product.entity;

import com.example.ecommerce_rest_api.features.category.entity.SubCategory;
import com.example.ecommerce_rest_api.features.review.entity.Review;
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
        name = "products",
        indexes = {
                @Index(name = "idx_product_slug", columnList = "slug"),
                @Index(name = "idx_product_name", columnList = "name"),
                @Index(name = "idx_product_active", columnList = "is_active"),
                @Index(name = "idx_product_featured", columnList = "is_featured"),
        }
)
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String slug;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String summary;

    @Column(name = "cover_image", length = 500)
    private String coverImage;

    private String brand;

    @Column(name = "is_active")
    private boolean isActive;

    @Column(name = "is_featured")
    private boolean isFeatured;

    @Column(name = "view_count")
    private Integer viewCount;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "product_categories",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "sub_category_id")
    )
    private List<SubCategory> subCategories = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductImage> images = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductSku> skus = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();

    public void addImage(ProductImage image){
        images.add(image);
        image.setProduct(this);
    }

    public void removeImage(ProductImage image){
        images.remove(image);
        image.setProduct(null);
    }

    public void addSku(ProductSku sku){
        skus.add(sku);
        sku.setProduct(this);
    }

    public void removeSku(ProductSku sku){
        skus.remove(sku);
        sku.setProduct(null);
    }

//    public Double getMinPrice() {
//        return skus.stream()
//                .filter(sku -> sku.getIsActive() && sku.getDeletedAt() == null)
//                .map(ProductSku::getPrice)
//                .min(Double::compareTo)
//                .orElse(0.0);
//    }
//
//    public Double getMaxPrice() {
//        return skus.stream()
//                .filter(sku -> sku.getIsActive() && sku.getDeletedAt() == null)
//                .map(ProductSku::getPrice)
//                .max(Double::compareTo)
//                .orElse(0.0);
//    }

    public Integer getTotalStock() {
        return skus.stream()
                .filter(sku -> sku.getIsActive() && sku.getDeletedAt() == null)
                .mapToInt(ProductSku::getQuantity)
                .sum();
    }

    public Double getAverageRating() {
        return reviews.stream()
                .filter(review -> review.getIsApproved() != null && review.getIsApproved())
                .mapToInt(Review::getRating)
                .average()
                .orElse(0.0);
    }

}
