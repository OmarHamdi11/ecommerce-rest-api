package com.example.ecommerce_rest_api.features.wishlist.service;

import com.example.ecommerce_rest_api.common.exception.ResourceNotFoundException;
import com.example.ecommerce_rest_api.features.product.entity.Product;
import com.example.ecommerce_rest_api.features.product.repository.ProductRepository;
import com.example.ecommerce_rest_api.features.user.entity.User;
import com.example.ecommerce_rest_api.features.user.repository.UserRepository;
import com.example.ecommerce_rest_api.features.wishlist.dto.WishlistDTO;
import com.example.ecommerce_rest_api.features.wishlist.dto.WishlistItemDTO;
import com.example.ecommerce_rest_api.features.wishlist.entity.Wishlist;
import com.example.ecommerce_rest_api.features.wishlist.entity.WishlistItem;
import com.example.ecommerce_rest_api.features.wishlist.repository.WishlistItemRepository;
import com.example.ecommerce_rest_api.features.wishlist.repository.WishlistRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WishlistServiceImpl implements WishlistService {

    private final WishlistRepository wishlistRepository;
    private final WishlistItemRepository wishlistItemRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public WishlistServiceImpl(
            WishlistRepository wishlistRepository,
            WishlistItemRepository wishlistItemRepository,
            UserRepository userRepository,
            ProductRepository productRepository
    ) {
        this.wishlistRepository = wishlistRepository;
        this.wishlistItemRepository = wishlistItemRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    @Override
    public WishlistDTO getWishlistByUserId(Long userId) {
        Wishlist wishlist = getOrCreateWishlist(userId);
        return mapToWishlistDTO(wishlist);
    }

    @Override
    @Transactional
    public WishlistDTO addItemToWishlist(Long userId, Long productId) {
        Wishlist wishlist = getOrCreateWishlist(userId);

        Product product = productRepository.findById(productId).orElseThrow(
                () -> new ResourceNotFoundException("Product", "id", productId)
        );

        if (!product.getIsActive()) {
            throw new RuntimeException("Product is not available");
        }

        // Check if product already in wishlist
        boolean exists = wishlist.getItems().stream().anyMatch(
                item -> item.getProduct().getId().equals(productId)
        );

        if (exists) {
            throw new RuntimeException("Product already in wishlist");
        }

        WishlistItem newItem = WishlistItem.builder()
                .wishlist(wishlist)
                .product(product)
                .build();

        wishlist.addItem(newItem);
        wishlistItemRepository.save(newItem);

        Wishlist savedWishlist = wishlistRepository.save(wishlist);
        return mapToWishlistDTO(savedWishlist);
    }

    @Override
    @Transactional
    public WishlistDTO removeItemFromWishlist(Long userId, Long productId) {
        Wishlist wishlist = getOrCreateWishlist(userId);

        WishlistItem item = wishlist.getItems().stream()
                .filter(i -> i.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("WishlistItem", "productId", productId));

        wishlist.removeItem(item);
        wishlistItemRepository.delete(item);

        Wishlist updatedWishlist = wishlistRepository.findById(wishlist.getId()).orElseThrow(
                () -> new ResourceNotFoundException("Wishlist", "id", wishlist.getId())
        );

        return mapToWishlistDTO(updatedWishlist);
    }

    @Override
    public Boolean isProductInWishlist(Long userId, Long productId) {
        Wishlist wishlist = getOrCreateWishlist(userId);

        return wishlist.getItems().stream()
                .anyMatch(item -> item.getProduct().getId().equals(productId));
    }

    @Override
    @Transactional
    public void clearWishlist(Long userId) {
        Wishlist wishlist = getOrCreateWishlist(userId);
        wishlistItemRepository.deleteAll(wishlist.getItems());
        wishlist.getItems().clear();
        wishlistRepository.save(wishlist);
    }

    // Helper methods
    private Wishlist getOrCreateWishlist(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("User", "id", userId)
        );

        return wishlistRepository.findByUserId(userId)
                .orElseGet(() -> {
                    Wishlist newWishlist = Wishlist.builder()
                            .user(user)
                            .build();
                    return wishlistRepository.save(newWishlist);
                });
    }

    private WishlistDTO mapToWishlistDTO(Wishlist wishlist) {
        List<WishlistItemDTO> items = wishlist.getItems().stream()
                .map(this::mapToWishlistItemDTO)
                .toList();

        return WishlistDTO.builder()
                .id(wishlist.getId())
                .userId(wishlist.getUser().getId())
                .items(items)
                .totalItems(items.size())
                .createdAt(wishlist.getCreatedAt())
                .updatedAt(wishlist.getUpdatedAt())
                .build();
    }

    private WishlistItemDTO mapToWishlistItemDTO(WishlistItem item) {
        Product product = item.getProduct();

        return WishlistItemDTO.builder()
                .id(item.getId())
                .productId(product.getId())
                .productName(product.getName())
                .productSlug(product.getSlug())
                .productImage(product.getCoverImage())
                .price(product.getMinPrice())
                .isAvailable(product.getIsActive() && product.getTotalStock() > 0)
                .addedAt(item.getAddedAt())
                .build();
    }
}
