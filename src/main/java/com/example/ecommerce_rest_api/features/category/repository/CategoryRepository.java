package com.example.ecommerce_rest_api.features.category.repository;

import com.example.ecommerce_rest_api.features.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryRepository  extends JpaRepository<Category, Long> {

    boolean existsByName(String name);

    @Query("select c from Category c where c.deleted_at is null")
    List<Category> findAllActive();
}
