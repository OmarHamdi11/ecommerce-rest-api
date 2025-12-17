package com.example.ecommerce_rest_api.features.product.service;

import com.example.ecommerce_rest_api.common.response.PageResponse;
import com.example.ecommerce_rest_api.features.product.dto.ProductCreateRequest;
import com.example.ecommerce_rest_api.features.product.dto.ProductDTO;
import com.example.ecommerce_rest_api.features.product.dto.ProductSearchRequest;
import com.example.ecommerce_rest_api.features.product.dto.ProductUpdateRequest;
import org.springframework.web.multipart.MultipartFile;

public interface ProductService {

    ProductDTO createProduct(ProductCreateRequest request, MultipartFile coverImage);
    ProductDTO updateProduct(Long productId, ProductUpdateRequest request);
    ProductDTO getProductById(Long productId);
    ProductDTO getProductBySlug(String slug);
    PageResponse<ProductDTO> getAllProducts(int pageNo, int pageSize, String sortBy, String sortDir);
    PageResponse<ProductDTO> searchProducts(ProductSearchRequest request,int pageNo, int pageSize);
    void deleteProduct(Long productId);
    void softDeleteProduct(Long productId);
}
