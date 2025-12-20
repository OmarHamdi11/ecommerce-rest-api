package com.example.ecommerce_rest_api.features.product.controller;

import com.example.ecommerce_rest_api.common.response.ResponseApi;
import com.example.ecommerce_rest_api.features.product.dto.ProductSkuCreateRequest;
import com.example.ecommerce_rest_api.features.product.dto.ProductSkuDTO;
import com.example.ecommerce_rest_api.features.product.dto.ProductSkuUpdateRequest;
import com.example.ecommerce_rest_api.features.product.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/products")
@Tag(name = "Product SKU Management", description = "APIs for managing product SKUs")
public class ProductSkuController {

    private final ProductService productService;

    public ProductSkuController(ProductService productService) {
        this.productService = productService;
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
}
