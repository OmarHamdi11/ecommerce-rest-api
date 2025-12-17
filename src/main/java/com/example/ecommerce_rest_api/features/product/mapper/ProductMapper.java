package com.example.ecommerce_rest_api.features.product.mapper;

import com.example.ecommerce_rest_api.features.category.entity.SubCategory;
import com.example.ecommerce_rest_api.features.product.dto.ProductAttributeDTO;
import com.example.ecommerce_rest_api.features.product.dto.ProductDTO;
import com.example.ecommerce_rest_api.features.product.dto.ProductImageDTO;
import com.example.ecommerce_rest_api.features.product.dto.ProductSkuDTO;
import com.example.ecommerce_rest_api.features.product.entity.Product;
import com.example.ecommerce_rest_api.features.product.entity.ProductAttribute;
import com.example.ecommerce_rest_api.features.product.entity.ProductImage;
import com.example.ecommerce_rest_api.features.product.entity.ProductSku;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductMapper {

    private final ModelMapper modelMapper;

    public ProductMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public ProductDTO toDTO(Product product){
        List<String> categoryNames = product.getSubCategories().stream()
                .map(SubCategory::getName)
                .toList();

        List<ProductImageDTO> images = product.getImages().stream()
                .map(this::mapToImageDTO)
                .toList();

        List<ProductSkuDTO> skus = product.getSkus().stream()
                .filter(sku -> sku.getDeletedAt() == null)
                .map(this::mapToSkuDTO)
                .toList();

        ProductDTO productDTO = modelMapper.map(product, ProductDTO.class);
        productDTO.setAverageRating(product.getAverageRating() != null ? product.getAverageRating() : 0.0);
        productDTO.setReviewCount(product.getReviews().size());
        productDTO.setSkus(skus);
        productDTO.setImages(images);
        productDTO.setMinPrice(product.getMinPrice());
        productDTO.setMaxPrice(product.getMaxPrice());
        productDTO.setTotalStock(product.getTotalStock());
        productDTO.setCategoryNames(categoryNames);

        return productDTO;
    }

    private ProductImageDTO mapToImageDTO(ProductImage productImage){
        return modelMapper.map(productImage, ProductImageDTO.class);
    }

    private ProductSkuDTO mapToSkuDTO(ProductSku productSku){
        List<ProductAttributeDTO> attributes = productSku.getAttributes().stream()
                .map(this::mapToAttributeDTO)
                .toList();

        ProductSkuDTO productSkuDTO = modelMapper.map(productSku, ProductSkuDTO.class);
        productSkuDTO.setIsLowStock(productSku.isLowStock());
        productSkuDTO.setIsOutOfStock(productSku.isOutOfStock());
        productSkuDTO.setDiscountPercentage(productSku.getDiscountPercentage());
        productSkuDTO.setAttributes(attributes);

        return productSkuDTO;
    }

    private ProductAttributeDTO mapToAttributeDTO(ProductAttribute productAttribute){
        return modelMapper.map(productAttribute,ProductAttributeDTO.class);
    }
}
