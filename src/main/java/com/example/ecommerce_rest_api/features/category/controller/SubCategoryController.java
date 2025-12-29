package com.example.ecommerce_rest_api.features.category.controller;

import com.example.ecommerce_rest_api.common.response.ResponseApi;
import com.example.ecommerce_rest_api.features.category.dto.SubCategoryCreateRequest;
import com.example.ecommerce_rest_api.features.category.dto.SubCategoryDTO;
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
@Tag(name = "SubCategory Management", description = "APIs for managing SubCategories")
public class SubCategoryController {

    private final CategoryService categoryService;

    public SubCategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Operation(summary = "Create subcategory", description = "Create new subcategory. Admin only.")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{categoryId}/subcategories")
    public ResponseEntity<ResponseApi<SubCategoryDTO>> createSubCategory(
            @PathVariable Long categoryId,
            @Valid @RequestBody SubCategoryCreateRequest request
    ) {
        SubCategoryDTO subCategory = categoryService.createSubCategory(categoryId, request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ResponseApi.created("SubCategory created successfully", subCategory));
    }

    @Operation(summary = "Update subcategory", description = "Update subcategory details. Admin only.")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/subcategories/{id}")
    public ResponseEntity<ResponseApi<SubCategoryDTO>> updateSubCategory(
            @PathVariable Long id,
            @Valid @RequestBody SubCategoryCreateRequest request
    ) {
        SubCategoryDTO subCategory = categoryService.updateSubCategory(id, request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseApi.success("SubCategory updated successfully", subCategory));
    }

    @Operation(summary = "Get subcategories by category", description = "Get all subcategories for a category")
    @GetMapping("/{categoryId}/subcategories")
    public ResponseEntity<ResponseApi<List<SubCategoryDTO>>> getSubCategoriesByCategory(
            @PathVariable Long categoryId
    ) {
        List<SubCategoryDTO> subCategories = categoryService.getSubCategoriesByCategory(categoryId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseApi.success("SubCategories retrieved successfully", subCategories));
    }

    @Operation(summary = "Delete subcategory", description = "Soft delete subcategory. Admin only.")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/subcategories/{id}")
    public ResponseEntity<ResponseApi<String>> deleteSubCategory(@PathVariable Long id) {
        categoryService.deleteSubCategory(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseApi.success("SubCategory deleted successfully", null));
    }
}
