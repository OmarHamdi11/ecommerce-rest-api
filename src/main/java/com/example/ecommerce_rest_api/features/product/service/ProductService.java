package com.example.ecommerce_rest_api.features.product.service;

import com.example.ecommerce_rest_api.common.response.PageResponse;
import com.example.ecommerce_rest_api.features.product.dto.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {

    // Product CRUD
    ProductDTO createProduct(ProductCreateRequest request, MultipartFile coverImage);
    ProductDTO updateProduct(Long productId, ProductUpdateRequest request);
    ProductDTO getProductById(Long productId);
    ProductDTO getProductBySlug(String slug);
    PageResponse<ProductDTO> getAllProducts(int pageNo, int pageSize, String sortBy, String sortDir);
    PageResponse<ProductDTO> searchProducts(ProductSearchRequest request,int pageNo, int pageSize);
    void deleteProduct(Long productId);
    void softDeleteProduct(Long productId);

    // Product Images
    ProductImageDTO addProductImage(Long productId, MultipartFile image);
    List<ProductImageDTO> addProductImages(Long productId, List<MultipartFile> images);
    void setPrimaryImage(Long productId, Long imageId);
    void deleteImage(Long imageId);

    // Product SKU
    ProductSkuDTO addProductSku(Long productId, ProductSkuCreateRequest request);
    ProductSkuDTO updateProductSku(Long skuId, ProductSkuUpdateRequest request);
    void deleteProductSku(Long skuId);
    ProductSkuDTO getSkuById(Long skuId);
    List<ProductSkuDTO> getProductSkus(Long productId);

}
