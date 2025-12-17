package com.example.ecommerce_rest_api.features.product.service;

import com.example.ecommerce_rest_api.common.exception.ResourceNotFoundException;
import com.example.ecommerce_rest_api.common.response.ImgBBUploadResponse;
import com.example.ecommerce_rest_api.common.response.PageResponse;
import com.example.ecommerce_rest_api.common.service.ImgBBService;
import com.example.ecommerce_rest_api.features.category.entity.SubCategory;
import com.example.ecommerce_rest_api.features.category.repository.SubCategoryRepository;
import com.example.ecommerce_rest_api.features.product.dto.*;
import com.example.ecommerce_rest_api.features.product.entity.Product;
import com.example.ecommerce_rest_api.features.product.entity.ProductAttribute;
import com.example.ecommerce_rest_api.features.product.entity.ProductImage;
import com.example.ecommerce_rest_api.features.product.entity.ProductSku;
import com.example.ecommerce_rest_api.features.product.mapper.ProductMapper;
import com.example.ecommerce_rest_api.features.product.repository.ProductAttributeRepository;
import com.example.ecommerce_rest_api.features.product.repository.ProductImageRepository;
import com.example.ecommerce_rest_api.features.product.repository.ProductRepository;
import com.example.ecommerce_rest_api.features.product.repository.ProductSkuRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService{

    private final ProductRepository productRepository;
    private final ProductSkuRepository productSkuRepository;
    private final ProductImageRepository productImageRepository;
    private final ProductAttributeRepository productAttributeRepository;
    private final SubCategoryRepository subCategoryRepository;
    private final ImgBBService imgBBService;
    private final ProductMapper productMapper;

    public ProductServiceImpl(ProductRepository productRepository,
                              ProductSkuRepository productSkuRepository,
                              ProductImageRepository productImageRepository,
                              ProductAttributeRepository productAttributeRepository,
                              SubCategoryRepository subCategoryRepository,
                              ImgBBService imgBBService,
                              ProductMapper productMapper
    ) {
        this.productRepository = productRepository;
        this.productSkuRepository = productSkuRepository;
        this.productImageRepository = productImageRepository;
        this.productAttributeRepository = productAttributeRepository;
        this.subCategoryRepository = subCategoryRepository;
        this.imgBBService = imgBBService;
        this.productMapper = productMapper;
    }

    // =============== Product CRUD Methods ===============

    @Override
    @Transactional
    public ProductDTO createProduct(ProductCreateRequest request, MultipartFile coverImage) {

        List<SubCategory> subCategories =
                subCategoryRepository.findAllById(request.getSubCategoryIds());
        if (subCategories.size() != request.getSubCategoryIds().size()) {
            throw new ResourceNotFoundException("SubCategory", "id", 0);
        }

        String slug = generateSlug(request.getName());

        Product product = Product.builder()
                .name(request.getName())
                .slug(slug)
                .description(request.getDescription())
                .summary(request.getSummary())
                .brand(request.getBrand())
                .isActive(request.getIsActive())
                .isFeatured(request.getIsFeatured())
                .viewCount(0)
                .build();

        product.setSubCategories(subCategories);

        if (coverImage != null && !coverImage.isEmpty()) {
            ImgBBUploadResponse uploadResponse = imgBBService.uploadImage(coverImage);

            product.setCoverImage(uploadResponse.getData().getDisplay_url());

            ProductImage image = new ProductImage();
            image.setImageUrl(uploadResponse.getData().getDisplay_url());
            image.setImageDeleteUrl(uploadResponse.getData().getDelete_url());
            image.setImageId(uploadResponse.getData().getId());
            image.setIsPrimary(true);

            product.addImage(image);
        }

        for (ProductSkuCreateRequest skuRequest : request.getSkus()) {

            if (productSkuRepository.existsBySku(skuRequest.getSku())) {
                throw new RuntimeException("SKU already exists: " + skuRequest.getSku());
            }

            ProductSku sku = ProductSku.builder()
                    .sku(skuRequest.getSku())
                    .price(skuRequest.getPrice())
                    .compareAtPrice(skuRequest.getCompareAtPrice())
                    .costPrice(skuRequest.getCostPrice())
                    .quantity(skuRequest.getQuantity())
                    .lowStockThreshold(skuRequest.getLowStockThreshold())
                    .weight(skuRequest.getWeight())
                    .isActive(skuRequest.getIsActive())
                    .build();

            if (skuRequest.getAttributeIds() != null && !skuRequest.getAttributeIds().isEmpty()) {
                List<ProductAttribute> attributes =
                        productAttributeRepository.findAllById(skuRequest.getAttributeIds());
                sku.setAttributes(attributes);
            }

            product.addSku(sku);
        }

        Product savedProduct = productRepository.save(product);

        return productMapper.toDTO(savedProduct);
    }

    @Override
    @Transactional
    public ProductDTO updateProduct(Long productId, ProductUpdateRequest request) {
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new ResourceNotFoundException("Product", "id", productId)
        );

        if (request.getName() != null){
            if (!request.getName().equals(product.getName())){
                product.setSlug(generateSlug(request.getName()));
            }
        }

        if (request.getDescription() != null){
            product.setDescription(request.getDescription());
        }

        if (request.getSummary() != null){
            product.setSummary(request.getSummary());
        }

        if (request.getBrand() != null){
            product.setBrand(request.getBrand());
        }

        if (request.getIsActive() != null){
            product.setIsActive(request.getIsActive());
        }

        if (request.getIsFeatured() != null){
            product.setIsFeatured(request.getIsFeatured());
        }

        if (request.getSubCategoryIds() != null && !request.getSubCategoryIds().isEmpty()){
            List<SubCategory> subCategories =
                    subCategoryRepository.findAllById(request.getSubCategoryIds());
            if (subCategories.size() != request.getSubCategoryIds().size()) {
                throw new ResourceNotFoundException("SubCategory", "id", 0);
            }
            product.setSubCategories(subCategories);
        }

        Product savedProduct = productRepository.save(product);
        return productMapper.toDTO(savedProduct);
    }

    @Override
    public ProductDTO getProductById(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new ResourceNotFoundException("Product", "id", productId)
        );
        incrementViewCount(product);
        return productMapper.toDTO(product);
    }

    @Override
    public ProductDTO getProductBySlug(String slug) {
        Product product = productRepository.findBySlug(slug).orElseThrow(
                () -> new ResourceNotFoundException("Product", "slug", 0L)
        );
        incrementViewCount(product);
        return productMapper.toDTO(product);
    }

    @Override
    public PageResponse<ProductDTO> getAllProducts(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo,pageSize, sort);

        Page<Product> productPage = productRepository.findAllActive(pageable);
        Page<ProductDTO> productDTOPage =productPage.map(productMapper::toDTO);

        return new PageResponse<>(productDTOPage);
    }

    @Override
    public PageResponse<ProductDTO> searchProducts(ProductSearchRequest request,int pageNo, int pageSize) {
        String sortBy = request.getSortBy() != null ? request.getSortBy() : "createdAt";
        String sortDir = request.getSortDirection() != null ? request.getSortDirection() : "DESC";

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNo,pageSize, sort);

        Page<Product> productPage = productRepository.searchByKeyword(request.getKeyword(), pageable);
        Page<ProductDTO> productDTOPage =productPage.map(productMapper::toDTO);

        return new PageResponse<>(productDTOPage);
    }

    @Override
    @Transactional
    public void deleteProduct(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new ResourceNotFoundException("Product", "id", productId)
        );
        productRepository.delete(product);
    }

    @Override
    public void softDeleteProduct(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new ResourceNotFoundException("Product", "id", productId)
        );
        product.setDeletedAt(LocalDateTime.now());
        productRepository.save(product);
    }

    // =============== Helper Methods ===============
    private String generateSlug(String name) {
        String baseSlug = name.toLowerCase()
                .replaceAll("[^a-z0-9\\s-]", "")
                .replaceAll("\\s+", "-")
                .replaceAll("-+", "-")
                .trim();

        String slug = baseSlug;
        int counter = 1;
        while (productRepository.existsBySlug(slug)) {
            slug = baseSlug + "-" + counter++;
        }
        return slug;
    }

    private void incrementViewCount(Product product){
        product.setViewCount(product.getViewCount() + 1);
        productRepository.save(product);
    }


}
