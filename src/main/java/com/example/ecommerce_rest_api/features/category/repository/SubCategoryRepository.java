package com.example.ecommerce_rest_api.features.category.repository;

import com.example.ecommerce_rest_api.features.category.entity.SubCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SubCategoryRepository extends JpaRepository<SubCategory,Long> {
    List<SubCategory> findByName(String name);

    boolean existsByName(String name);

    @Query("select sc from SubCategory sc where sc.category.id=:categoryId and sc.deleted_at is null ")
    List<SubCategory> findByCategoryId(@Param("categoryId") Long categoryId);

    @Query("select sc from SubCategory sc where sc.deleted_at is null ")
    List<SubCategory> findAllActive();

}
