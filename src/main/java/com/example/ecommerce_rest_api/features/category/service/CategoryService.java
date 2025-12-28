package com.example.ecommerce_rest_api.features.category.service;

import com.example.ecommerce_rest_api.features.category.dto.CategoryCreateRequest;
import com.example.ecommerce_rest_api.features.category.dto.CategoryDTO;
import com.example.ecommerce_rest_api.features.category.dto.SubCategoryCreateRequest;
import com.example.ecommerce_rest_api.features.category.dto.SubCategoryDTO;

import java.util.List;

public interface CategoryService {
    CategoryDTO createCategory(CategoryCreateRequest request);
    CategoryDTO updateCategory(Long id, CategoryCreateRequest request);
    CategoryDTO getCategoryById(Long id);
    List<CategoryDTO> getAllCategories();
    void deleteCategory(Long id);

    SubCategoryDTO createSubCategory(Long categoryId, SubCategoryCreateRequest request);
    SubCategoryDTO updateSubCategory(Long id, SubCategoryCreateRequest request);
    List<SubCategoryDTO> getSubCategoriesByCategory(Long categoryId);
    void deleteSubCategory(Long id);
}
