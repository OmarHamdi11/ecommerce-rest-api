package com.example.ecommerce_rest_api.features.category.entity;

import com.example.ecommerce_rest_api.features.product.entity.Product;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "sub_categories"
)
public class SubCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private LocalDateTime created_at;
    private LocalDateTime deleted_at;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Category category;

    @ManyToMany(
            mappedBy = "subCategories",
            fetch = FetchType.LAZY,
            cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}
    )
    private List<Product> products;
}
