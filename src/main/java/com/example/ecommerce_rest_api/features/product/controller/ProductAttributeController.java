package com.example.ecommerce_rest_api.features.product.controller;

import com.example.ecommerce_rest_api.common.response.ResponseApi;
import com.example.ecommerce_rest_api.features.product.dto.ProductAttributeCreateRequest;
import com.example.ecommerce_rest_api.features.product.dto.ProductAttributeDTO;
import com.example.ecommerce_rest_api.features.product.service.ProductService;
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
@RequestMapping("api/v1/products/attributes")
@Tag(name = "Product Attributes", description = "APIs for managing product attributes")
public class ProductAttributeController {

    private final ProductService productService;

    public ProductAttributeController(ProductService productService) {
        this.productService = productService;
    }


    @Operation(summary = "Create attribute", description = "Create new product attribute. Admin only.")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping()
    public ResponseEntity<ResponseApi<ProductAttributeDTO>> createAttribute(
            @Valid @RequestBody ProductAttributeCreateRequest request
    ) {
        ProductAttributeDTO attribute = productService.createAttribute(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ResponseApi.created("Attribute created", attribute));
    }

    @Operation(summary = "Get all attributes", description = "Get all product attributes")
    @GetMapping()
    public ResponseEntity<ResponseApi<List<ProductAttributeDTO>>> getAllAttributes() {
        List<ProductAttributeDTO> attributes = productService.getAllAttributes();
        return ResponseEntity.ok(ResponseApi.success("Attributes retrieved", attributes));
    }

    @Operation(summary = "Get attributes by type", description = "Get attributes filtered by type")
    @GetMapping("/type/{type}")
    public ResponseEntity<ResponseApi<List<ProductAttributeDTO>>> getAttributesByType(
            @PathVariable String type
    ) {
        List<ProductAttributeDTO> attributes = productService.getAttributesByType(type);
        return ResponseEntity.ok(ResponseApi.success("Attributes retrieved", attributes));
    }
}
