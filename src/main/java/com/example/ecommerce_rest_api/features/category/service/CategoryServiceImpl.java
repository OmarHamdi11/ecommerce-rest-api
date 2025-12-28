package com.example.ecommerce_rest_api.features.category.service;

import com.example.ecommerce_rest_api.common.exception.ResourceNotFoundException;
import com.example.ecommerce_rest_api.features.category.dto.CategoryCreateRequest;
import com.example.ecommerce_rest_api.features.category.dto.CategoryDTO;
import com.example.ecommerce_rest_api.features.category.dto.SubCategoryCreateRequest;
import com.example.ecommerce_rest_api.features.category.dto.SubCategoryDTO;
import com.example.ecommerce_rest_api.features.category.entity.Category;
import com.example.ecommerce_rest_api.features.category.entity.SubCategory;
import com.example.ecommerce_rest_api.features.category.repository.CategoryRepository;
import com.example.ecommerce_rest_api.features.category.repository.SubCategoryRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService{
    private final CategoryRepository categoryRepository;
    private final SubCategoryRepository subCategoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository,
                               SubCategoryRepository subCategoryRepository) {
        this.categoryRepository = categoryRepository;
        this.subCategoryRepository = subCategoryRepository;
    }

    @Override
    @Transactional
    public CategoryDTO createCategory(CategoryCreateRequest request) {
        if (categoryRepository.existsByName(request.getName())) {
            throw new RuntimeException("Category already exists with name: " + request.getName());
        }

        Category category = new Category();
        category.setName(request.getName());
        category.setDescription(request.getDescription());
        category.setCreated_at(LocalDateTime.now());

        Category savedCategory = categoryRepository.save(category);
        return mapToCategoryDTO(savedCategory);
    }

    @Override
    @Transactional
    public CategoryDTO updateCategory(Long id, CategoryCreateRequest request) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));

        if (!category.getName().equals(request.getName()) &&
                categoryRepository.existsByName(request.getName())) {
            throw new RuntimeException("Category already exists with name: " + request.getName());
        }

        category.setName(request.getName());
        category.setDescription(request.getDescription());

        Category savedCategory = categoryRepository.save(category);
        return mapToCategoryDTO(savedCategory);
    }

    @Override
    public CategoryDTO getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));
        return mapToCategoryDTO(category);
    }

    @Override
    public List<CategoryDTO> getAllCategories() {
        List<Category> categories = categoryRepository.findAllActive();
        return categories.stream()
                .map(this::mapToCategoryDTO)
                .toList();
    }

    @Override
    @Transactional
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));

        category.setDeleted_at(LocalDateTime.now());

        // Soft delete all subcategories
        category.getSubCategories().forEach(sub -> sub.setDeleted_at(LocalDateTime.now()));

        categoryRepository.save(category);
    }

    @Override
    @Transactional
    public SubCategoryDTO createSubCategory(Long categoryId, SubCategoryCreateRequest request) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", categoryId));

        if (subCategoryRepository.existsByName(request.getName())) {
            throw new RuntimeException("SubCategory already exists with name: " + request.getName());
        }

        SubCategory subCategory = new SubCategory();
        subCategory.setName(request.getName());
        subCategory.setDescription(request.getDescription());
        subCategory.setCategory(category);
        subCategory.setCreated_at(LocalDateTime.now());

        SubCategory savedSubCategory = subCategoryRepository.save(subCategory);
        return mapToSubCategoryDTO(savedSubCategory);
    }

    @Override
    @Transactional
    public SubCategoryDTO updateSubCategory(Long id, SubCategoryCreateRequest request) {
        SubCategory subCategory = subCategoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SubCategory", "id", id));

        if (!subCategory.getName().equals(request.getName()) &&
                subCategoryRepository.existsByName(request.getName())) {
            throw new RuntimeException("SubCategory already exists with name: " + request.getName());
        }

        subCategory.setName(request.getName());
        subCategory.setDescription(request.getDescription());

        SubCategory savedSubCategory = subCategoryRepository.save(subCategory);
        return mapToSubCategoryDTO(savedSubCategory);
    }

    @Override
    public List<SubCategoryDTO> getSubCategoriesByCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", categoryId));

        List<SubCategory> subCategories = subCategoryRepository.findByCategoryId(categoryId);
        return subCategories.stream()
                .map(this::mapToSubCategoryDTO)
                .toList();
    }

    @Override
    @Transactional
    public void deleteSubCategory(Long id) {
        SubCategory subCategory = subCategoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SubCategory", "id", id));

        subCategory.setDeleted_at(LocalDateTime.now());
        subCategoryRepository.save(subCategory);
    }

    // Helper methods
    private CategoryDTO mapToCategoryDTO(Category category) {
        List<SubCategoryDTO> subCategories = category.getSubCategories().stream()
                .filter(sub -> sub.getDeleted_at() == null)
                .map(this::mapToSubCategoryDTO)
                .toList();

        return CategoryDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .createdAt(category.getCreated_at())
                .subCategories(subCategories)
                .build();
    }

    private SubCategoryDTO mapToSubCategoryDTO(SubCategory subCategory) {
        return SubCategoryDTO.builder()
                .id(subCategory.getId())
                .name(subCategory.getName())
                .description(subCategory.getDescription())
                .categoryId(subCategory.getCategory().getId())
                .categoryName(subCategory.getCategory().getName())
                .createdAt(subCategory.getCreated_at())
                .build();
    }
}
