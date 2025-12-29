package com.example.ecommerce_rest_api.features.category.controller;

import com.example.ecommerce_rest_api.common.response.ResponseApi;
import com.example.ecommerce_rest_api.features.category.dto.CategoryCreateRequest;
import com.example.ecommerce_rest_api.features.category.dto.CategoryDTO;
import com.example.ecommerce_rest_api.features.category.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
@Tag(name = "Category Management", description = "APIs for managing Categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Operation(summary = "Create category", description = "Create new category. Admin only.")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ResponseApi<CategoryDTO>> createCategory(
            @Valid @RequestBody CategoryCreateRequest request
    ) {
        CategoryDTO category = categoryService.createCategory(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ResponseApi.created("Category created successfully", category));
    }

    @Operation(summary = "Update category", description = "Update category details. Admin only.")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ResponseApi<CategoryDTO>> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody CategoryCreateRequest request
    ) {
        CategoryDTO category = categoryService.updateCategory(id, request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseApi.success("Category updated successfully", category));
    }

    @Operation(summary = "Get category by ID", description = "Get category details with subcategories")
    @GetMapping("/{id}")
    public ResponseEntity<ResponseApi<CategoryDTO>> getCategoryById(@PathVariable Long id) {
        CategoryDTO category = categoryService.getCategoryById(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseApi.success("Category retrieved successfully", category));
    }

    @Operation(summary = "Get all categories", description = "Get all active categories with subcategories")
    @GetMapping
    public ResponseEntity<ResponseApi<List<CategoryDTO>>> getAllCategories() {
        List<CategoryDTO> categories = categoryService.getAllCategories();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseApi.success("Categories retrieved successfully", categories));
    }

    @Operation(summary = "Delete category", description = "Soft delete category. Admin only.")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseApi<String>> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseApi.success("Category deleted successfully", null));
    }
}
