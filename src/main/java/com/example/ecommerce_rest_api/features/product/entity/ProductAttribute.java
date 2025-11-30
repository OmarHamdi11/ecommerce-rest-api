package com.example.ecommerce_rest_api.features.product.entity;

import com.example.ecommerce_rest_api.features.product.ENUM.AttributeType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "product_attributes",
        indexes = {
                @Index(name = "idx_attribute_type", columnList = "type")
        }
)
public class ProductAttribute {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AttributeType type;

    @Column(nullable = false, length = 50)
    private String value;

    @Column(name = "display_value", length = 50)
    private String displayValue;

    @Column(name = "hex_code", length = 7)
    private String hexCode;

    @CreationTimestamp
    @Column(name = "created_at",updatable = false)
    private LocalDateTime createdAt;

    @ManyToMany(mappedBy = "attributes", fetch = FetchType.LAZY)
    private List<ProductSku> skus = new ArrayList<>();
}
