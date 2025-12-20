package com.example.ecommerce_rest_api.features.product.controller;

import com.example.ecommerce_rest_api.common.response.ResponseApi;
import com.example.ecommerce_rest_api.features.product.dto.ProductImageDTO;
import com.example.ecommerce_rest_api.features.product.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("api/v1/products")
@Tag(name = "Product Image Management", description = "APIs for managing product images")
public class ProductImageController {

    private final ProductService productService;

    public ProductImageController(ProductService productService) {
        this.productService = productService;
    }


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

}
