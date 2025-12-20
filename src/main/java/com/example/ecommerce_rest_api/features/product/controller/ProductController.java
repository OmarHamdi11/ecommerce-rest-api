package com.example.ecommerce_rest_api.features.product.controller;

import com.example.ecommerce_rest_api.common.response.PageResponse;
import com.example.ecommerce_rest_api.common.response.ResponseApi;
import com.example.ecommerce_rest_api.features.product.dto.*;
import com.example.ecommerce_rest_api.features.product.service.ProductService;
import com.example.ecommerce_rest_api.utils.AppConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("api/v1/products")
@Tag(name = "Product Management", description = "APIs for managing products, SKUs and images")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // ============= PRODUCT CRUD =============

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

    @Operation(summary = "Get featured products", description = "Retrieve featured products")
    @GetMapping("/featured")
    public ResponseEntity<ResponseApi<PageResponse<ProductDTO>>> getFeaturedProducts(
            @RequestParam(value = "pageNo" , defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize" , defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ) {
        PageResponse<ProductDTO> products = productService.getFeaturedProducts(pageNo,pageSize,sortBy,sortDir);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseApi.success("Featured products retrieved", products));
    }

    @Operation(summary = "Toggle featured status", description = "Toggle product featured status. Admin only.")
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{productId}/featured")
        public ResponseEntity<ResponseApi<String>> toggleFeatured(@PathVariable Long productId) {
        productService.toggleFeatured(productId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseApi.success("Featured status toggled", null));
    }

    @Operation(summary = "Toggle active status", description = "Toggle product active status. Admin only.")
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{productId}/active")
    public ResponseEntity<ResponseApi<String>> toggleActive(@PathVariable Long productId) {
        productService.toggleActive(productId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseApi.success("Active status toggled", null));
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

    @Operation(summary = "Get all brands", description = "Get list of all product brands")
    @GetMapping("/brands")
    public ResponseEntity<ResponseApi<List<String>>> getAllBrands() {
        List<String> brands = productService.getAllBrands();
        return ResponseEntity.ok(ResponseApi.success("Brands retrieved", brands));
    }


    // ============= PRODUCT IMAGES =============

    @Operation(summary = "Add product image", description = "Add single image to product. Admin only.")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "/{productId}/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseApi<ProductImageDTO>> addProductImage(
            @PathVariable("productId") Long productId,
            @RequestPart("image") MultipartFile image
    ){
        ProductImageDTO response = productService.addProductImage(productId,image);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseApi.success("Product image uploaded successfully",response));
    }

    @Operation(summary = "Add multiple images", description = "Add multiple images to product. Admin only.")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "/{productId}/images/bulk", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseApi<List<ProductImageDTO>>> addProductImages(
            @PathVariable("productId") Long productId,
            @RequestPart("images") List<MultipartFile> images
    ){
        List<ProductImageDTO> response = productService.addProductImages(productId,images);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseApi.success("Product images uploaded successfully",response));
    }

    @Operation(summary = "Set primary image", description = "Set an image as primary. Admin only.")
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{productId}/images/{imageId}/primary")
    public ResponseEntity<ResponseApi<String>> setPrimaryImage(
            @PathVariable("productId") Long productId,
            @PathVariable("imageId") Long imageId
    ){
        productService.setPrimaryImage(productId,imageId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseApi.success("Image set primary",null));
    }

    @Operation(summary = "Delete product image", description = "Delete product image. Admin only.")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/images/{imageId}")
    public ResponseEntity<ResponseApi<String>> deleteImage(
            @PathVariable("imageId") Long imageId
    ){
        productService.deleteImage(imageId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseApi.success("Image deleted successfully",null));
    }

    @Operation(summary = "Add product SKU", description = "Add new SKU to product. Admin only.")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{productId}/skus")
    public ResponseEntity<ResponseApi<ProductSkuDTO>> addProductSku(
            @PathVariable("productId") Long productId,
            @RequestBody ProductSkuCreateRequest request
    ){
        ProductSkuDTO response = productService.addProductSku(productId,request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseApi.success("Sku added successfully",response));
    }

    @Operation(summary = "Update product SKU", description = "Update SKU details. Admin only.")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/skus/{skuId}")
    public ResponseEntity<ResponseApi<ProductSkuDTO>> updateProductSku(
            @PathVariable Long skuId,
            @Valid @RequestBody ProductSkuUpdateRequest request
    ) {
        ProductSkuDTO response = productService.updateProductSku(skuId, request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseApi.success("Sku updated successfully",response));
    }

    @Operation(summary = "Get SKU by ID", description = "Retrieve SKU details by ID")
    @GetMapping("/skus/{skuId}")
    public ResponseEntity<ResponseApi<ProductSkuDTO>> getSkuById(@PathVariable Long skuId) {
        ProductSkuDTO sku = productService.getSkuById(skuId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseApi.success("SKU retrieved", sku));
    }

    @Operation(summary = "Get product SKUs", description = "Get all SKUs for a product")
    @GetMapping("/{productId}/skus")
    public ResponseEntity<ResponseApi<List<ProductSkuDTO>>> getProductSkus(@PathVariable Long productId) {
        List<ProductSkuDTO> skus = productService.getProductSkus(productId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseApi.success("SKUs retrieved", skus));
    }

    @Operation(summary = "Delete SKU", description = "Soft delete SKU. Admin only.")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/skus/{skuId}")
    public ResponseEntity<ResponseApi<String>> deleteProductSku(@PathVariable Long skuId) {
        productService.deleteProductSku(skuId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseApi.success("SKU deleted", null));
    }


    // ============= PRODUCT ATTRIBUTES =============

    @Operation(summary = "Create attribute", description = "Create new product attribute. Admin only.")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/attributes")
    public ResponseEntity<ResponseApi<ProductAttributeDTO>> createAttribute(
            @Valid @RequestBody ProductAttributeCreateRequest request
    ) {
        ProductAttributeDTO attribute = productService.createAttribute(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ResponseApi.created("Attribute created", attribute));
    }

    @Operation(summary = "Get all attributes", description = "Get all product attributes")
    @GetMapping("/attributes")
    public ResponseEntity<ResponseApi<List<ProductAttributeDTO>>> getAllAttributes() {
        List<ProductAttributeDTO> attributes = productService.getAllAttributes();
        return ResponseEntity.ok(ResponseApi.success("Attributes retrieved", attributes));
    }

    @Operation(summary = "Get attributes by type", description = "Get attributes filtered by type")
    @GetMapping("/attributes/type/{type}")
    public ResponseEntity<ResponseApi<List<ProductAttributeDTO>>> getAttributesByType(
            @PathVariable String type
    ) {
        List<ProductAttributeDTO> attributes = productService.getAttributesByType(type);
        return ResponseEntity.ok(ResponseApi.success("Attributes retrieved", attributes));
    }

}
