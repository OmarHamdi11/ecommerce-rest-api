package com.example.ecommerce_rest_api.features.product.controller;

import com.example.ecommerce_rest_api.common.response.PageResponse;
import com.example.ecommerce_rest_api.common.response.ResponseApi;
import com.example.ecommerce_rest_api.features.product.dto.ProductCreateRequest;
import com.example.ecommerce_rest_api.features.product.dto.ProductDTO;
import com.example.ecommerce_rest_api.features.product.dto.ProductSearchRequest;
import com.example.ecommerce_rest_api.features.product.dto.ProductUpdateRequest;
import com.example.ecommerce_rest_api.features.product.service.ProductService;
import com.example.ecommerce_rest_api.utils.AppConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/v1/products")
@Tag(name = "Product Management", description = "APIs for managing products, SKUs and images")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @Operation(
            summary = "Create new product",
            description = "Create a new product with cover image and SKUs. Admin only."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Product created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseApi<ProductDTO>> createProduct(
            @Valid @RequestPart("data") ProductCreateRequest request,
            @RequestPart(value = "coverImage", required = false) MultipartFile coverImage
    ){
        ProductDTO response = productService.createProduct(request,coverImage);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ResponseApi.created("Product created successfully",response));
    }

    @Operation(summary = "Update product", description = "Update product details. Admin only.")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ResponseApi<ProductDTO>> updateProduct(
            @PathVariable("id") Long productId,
            @RequestBody ProductUpdateRequest request
    ) {
        ProductDTO response = productService.updateProduct(productId, request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseApi.success("Product updated successfully",response));
    }

    @Operation(summary = "Get product by ID", description = "Retrieve detailed product information by ID")
    @GetMapping("/{id}")
    public ResponseEntity<ResponseApi<ProductDTO>> getProductById(
            @PathVariable("id") Long productId
    ) {
        ProductDTO response = productService.getProductById(productId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseApi.success("Product retrieved successfully",response));
    }

    @Operation(summary = "Get product by slug", description = "Retrieve product by SEO-friendly slug")
    @GetMapping("/slug/{slug}")
    public ResponseEntity<ResponseApi<ProductDTO>> getProductBySlug(
            @PathVariable("slug") String slug
    ) {
        ProductDTO response = productService.getProductBySlug(slug);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseApi.success("Product retrieved successfully",response));
    }

    @Operation(summary = "Get all products", description = "Retrieve paginated list of all products")
    @GetMapping()
    public ResponseEntity<ResponseApi<PageResponse<ProductDTO>>> getAllProducts(
            @RequestParam(value = "pageNo" , defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize" , defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ){
        PageResponse<ProductDTO> response =
                productService.getAllProducts(pageNo,pageSize,sortBy,sortDir);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseApi.success("Products retrieved successfully",response));
    }

    @Operation(summary = "Search products", description = "Search and filter products with multiple criteria")
    @PostMapping("/search")
    public ResponseEntity<ResponseApi<PageResponse<ProductDTO>>> searchProducts(
            @RequestBody ProductSearchRequest request,
            @RequestParam(value = "pageNo" , defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize" , defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize
    ){
        PageResponse<ProductDTO> response =
                productService.searchProducts(request, pageNo,pageSize);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseApi.success("Search completed successfully",response));
    }

    @Operation(summary = "Delete product permanently", description = "Permanently delete product. Admin only.")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseApi<String>> deleteProduct(
            @PathVariable("id") Long productId
    ){
        productService.deleteProduct(productId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseApi.success("Product deleted permanently",null));
    }

    @Operation(summary = "Soft delete product", description = "Soft delete product. Admin only.")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}/soft")
    public ResponseEntity<ResponseApi<String>> softDeleteProduct(
            @PathVariable("id") Long productId
    ){
        productService.softDeleteProduct(productId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseApi.success("Product soft deleted",null));
    }

}
