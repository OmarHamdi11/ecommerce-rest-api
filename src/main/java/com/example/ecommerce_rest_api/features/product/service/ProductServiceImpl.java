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
import java.util.ArrayList;
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

    @Override
    @Transactional
    public ProductImageDTO addProductImage(Long productId, MultipartFile image) {
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new ResourceNotFoundException("Product", "id", productId)
        );

        ImgBBUploadResponse uploadResponse = imgBBService.uploadImage(image);

        int displayOrder = product.getImages().stream()
                .mapToInt(ProductImage::getDisplayOrder)
                .max()
                .orElse(0) + 1;

        ProductImage productImage = ProductImage.builder()
                .imageUrl(uploadResponse.getData().getDisplay_url())
                .imageDeleteUrl(uploadResponse.getData().getDelete_url())
                .imageId(uploadResponse.getData().getId())
                .displayOrder(displayOrder)
                .isPrimary(product.getImages().isEmpty())
                .product(product)
                .build();

        ProductImage savedImage = productImageRepository.save(productImage);

        return productMapper.mapToImageDTO(savedImage);
    }

    @Override
    @Transactional
    public List<ProductImageDTO> addProductImages(Long productId, List<MultipartFile> images) {
        List<ProductImageDTO> result = new ArrayList<>();
        for (MultipartFile image : images){
            result.add(addProductImage(productId,image));
        }
        return result;
    }

    @Override
    @Transactional
    public void setPrimaryImage(Long productId, Long imageId) {
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new ResourceNotFoundException("Product", "id", productId)
        );

        product.getImages().forEach(img -> img.setIsPrimary(false));

        ProductImage newPrimary = productImageRepository.findById(imageId).orElseThrow(
                () -> new ResourceNotFoundException("ProductImage", "id", imageId)
        );

        if (!newPrimary.getProduct().getId().equals(product.getId())){
            throw new RuntimeException("Image does not belong to this product");
        }

        newPrimary.setIsPrimary(true);
        productImageRepository.save(newPrimary);
    }

    @Override
    public void deleteImage(Long imageId) {
        ProductImage image = productImageRepository.findById(imageId).orElseThrow(
                () -> new ResourceNotFoundException("ProductImage", "id", imageId)
        );

        productImageRepository.delete(image);

        if (image.getIsPrimary()){
            List<ProductImage> images =
                    productImageRepository.findByProductIdOrderByDisplayOrderAsc(image.getProduct().getId());
            if (!images.isEmpty()){
                images.getFirst().setIsPrimary(true);
                productImageRepository.save(images.getFirst());
            }
        }

    }

    @Override
    @Transactional
    public ProductSkuDTO addProductSku(Long productId, ProductSkuCreateRequest request) {
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new ResourceNotFoundException("Product", "id", productId)
        );

        if (productSkuRepository.existsBySku(request.getSku())) {
            throw new RuntimeException("SKU code already exists: " + request.getSku());
        }

        createSkuForProduct(product,request);

        ProductSku sku = productSkuRepository.findBySku(request.getSku())
                .orElseThrow(() -> new RuntimeException("Error creating SKU"));

        return productMapper.mapToSkuDTO(sku);
    }

    @Override
    @Transactional
    public ProductSkuDTO updateProductSku(Long skuId, ProductSkuUpdateRequest request) {
        ProductSku sku = productSkuRepository.findById(skuId).orElseThrow(
                () -> new ResourceNotFoundException("SKU", "id", skuId)
        );

        if (request.getPrice() != null){
            sku.setPrice(request.getPrice());
        }
        if (request.getCompareAtPrice() != null){
            sku.setCompareAtPrice(request.getCompareAtPrice());
        }
        if (request.getQuantity() != null){
            sku.setQuantity(request.getQuantity());
        }
        if (request.getIsActive() != null){
            sku.setIsActive(request.getIsActive());
        }
        if (request.getAttributeIds() != null && !request.getAttributeIds().isEmpty()){
            List<ProductAttribute> attributes = productAttributeRepository.findAllById(request.getAttributeIds());
            sku.setAttributes(attributes);
        }

        ProductSku savedSku = productSkuRepository.save(sku);

        return productMapper.mapToSkuDTO(savedSku);
    }

    @Override
    @Transactional
    public void deleteProductSku(Long skuId) {
        ProductSku sku = productSkuRepository.findById(skuId).orElseThrow(
                () -> new ResourceNotFoundException("SKU", "id", skuId)
        );
        sku.setIsActive(false);
        sku.setDeletedAt(LocalDateTime.now());
        productSkuRepository.save(sku);
    }

    @Override
    public ProductSkuDTO getSkuById(Long skuId) {
        ProductSku sku = productSkuRepository.findById(skuId).orElseThrow(
                () -> new ResourceNotFoundException("SKU", "id", skuId)
        );
        return productMapper.mapToSkuDTO(sku);
    }

    @Override
    public List<ProductSkuDTO> getProductSkus(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new ResourceNotFoundException("Product", "id", productId)
        );
        List<ProductSku> productSkuList = productSkuRepository.findByProductId(product.getId());
        return productSkuList.stream().map(productMapper::mapToSkuDTO).toList();
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


    private void createSkuForProduct(Product product, ProductSkuCreateRequest request) {
        // Get attributes
        List<ProductAttribute> attributes = new ArrayList<>();
        if (request.getAttributeIds() != null && !request.getAttributeIds().isEmpty()) {
            attributes = productAttributeRepository.findAllById(request.getAttributeIds());
        }

        ProductSku sku = ProductSku.builder()
                .sku(request.getSku())
                .price(request.getPrice())
                .compareAtPrice(request.getCompareAtPrice())
                .costPrice(request.getCostPrice())
                .quantity(request.getQuantity())
                .lowStockThreshold(request.getLowStockThreshold())
                .weight(request.getWeight())
                .isActive(request.getIsActive())
                .product(product)
                .attributes(attributes)
                .build();

        productSkuRepository.save(sku);
    }


}
