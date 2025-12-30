package com.example.ecommerce_rest_api.features.cart.service;

import com.example.ecommerce_rest_api.common.exception.ResourceNotFoundException;
import com.example.ecommerce_rest_api.features.cart.dto.CartItemDTO;
import com.example.ecommerce_rest_api.features.cart.entity.Cart;
import com.example.ecommerce_rest_api.features.cart.entity.CartItem;
import com.example.ecommerce_rest_api.features.cart.repository.CartItemRepository;
import com.example.ecommerce_rest_api.features.cart.repository.CartRepository;
import com.example.ecommerce_rest_api.features.product.entity.ProductSku;
import com.example.ecommerce_rest_api.features.product.repository.ProductSkuRepository;
import com.example.ecommerce_rest_api.features.user.entity.User;
import com.example.ecommerce_rest_api.features.user.repository.UserRepository;
import com.example.ecommerce_rest_api.features.cart.dto.CartDTO;
import com.example.ecommerce_rest_api.features.cart.dto.CartItemRequest;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CartServiceImpl implements CartService{

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final ProductSkuRepository productSkuRepository;

    public CartServiceImpl(CartRepository cartRepository,
                           CartItemRepository cartItemRepository,
                           UserRepository userRepository,
                           ProductSkuRepository productSkuRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.userRepository = userRepository;
        this.productSkuRepository = productSkuRepository;
    }

    @Override
    public CartDTO getCartByUserId(Long userId) {
        Cart cart = getOrCreateCart(userId);
        return mapToCartDTO(cart);
    }

    @Override
    @Transactional
    public CartDTO addItemToCart(Long userId, CartItemRequest request) {
        Cart cart = getOrCreateCart(userId);

        ProductSku productSku = productSkuRepository.findById(request.getSkuId()).orElseThrow(
                () -> new ResourceNotFoundException("ProductSku", "id", request.getSkuId())
        );

        if (!productSku.getIsActive()) {
            throw new RuntimeException("Product SKU is not available");
        }

        if (productSku.getQuantity() < request.getQuantity()) {
            throw new RuntimeException("Insufficient stock. Available: " + productSku.getQuantity());
        }

        // Check if item already exists in cart
        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(item -> item.getProductSku().getId().equals(request.getSkuId()))
                .findFirst();

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            int newQuantity = item.getQuantity() + request.getQuantity();

            if (productSku.getQuantity() < newQuantity) {
                throw new RuntimeException("Insufficient stock. Available: " + productSku.getQuantity());
            }

            item.setQuantity(newQuantity);
            cartItemRepository.save(item);
        } else {
            CartItem newItem = CartItem.builder()
                    .cart(cart)
                    .productSku(productSku)
                    .quantity(request.getQuantity())
                    .price(productSku.getPrice())
                    .build();

            cart.addItem(newItem);
            cartItemRepository.save(newItem);
        }

        Cart savedCart = cartRepository.save(cart);
        return mapToCartDTO(savedCart);
    }

    @Override
    @Transactional
    public CartDTO updateCartItemQuantity(Long userId, Long itemId, Integer quantity) {
        Cart cart = getOrCreateCart(userId);

        CartItem cartItem = cartItemRepository.findById(itemId).orElseThrow(
                () -> new ResourceNotFoundException("CartItem", "id", itemId)
        );

        if (!cartItem.getCart().getId().equals(cart.getId())) {
            throw new RuntimeException("Cart item does not belong to this user");
        }

        if (quantity <= 0) {
            throw new RuntimeException("Quantity must be greater than 0");
        }

        ProductSku productSku = cartItem.getProductSku();
        if (productSku.getQuantity() < quantity) {
            throw new RuntimeException("Insufficient stock. Available: " + productSku.getQuantity());
        }

        cartItem.setQuantity(quantity);
        cartItem.setPrice(productSku.getPrice()); // Update price in case it changed
        cartItemRepository.save(cartItem);

        Cart updatedCart = cartRepository.findById(cart.getId()).orElseThrow(
                () -> new ResourceNotFoundException("Cart", "id", cart.getId())
        );

        return mapToCartDTO(updatedCart);
    }

    @Override
    @Transactional
    public CartDTO removeItemFromCart(Long userId, Long itemId) {
        Cart cart = getOrCreateCart(userId);

        CartItem cartItem = cartItemRepository.findById(itemId).orElseThrow(
                () -> new ResourceNotFoundException("CartItem", "id", itemId)
        );

        if (!cartItem.getCart().getId().equals(cart.getId())) {
            throw new RuntimeException("Cart item does not belong to this user");
        }

        cart.removeItem(cartItem);
        cartItemRepository.delete(cartItem);

        Cart updatedCart = cartRepository.findById(cart.getId()).orElseThrow(
                () -> new ResourceNotFoundException("Cart", "id", cart.getId())
        );

        return mapToCartDTO(updatedCart);
    }

    @Override
    @Transactional
    public void clearCart(Long userId) {
        Cart cart = getOrCreateCart(userId);
        cartItemRepository.deleteAll(cart.getItems());
        cart.getItems().clear();
        cartRepository.save(cart);
    }

    // Helper methods
    private Cart getOrCreateCart(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("User", "id", userId)
        );

        return cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    Cart newCart = Cart.builder()
                            .user(user)
                            .build();
                    return cartRepository.save(newCart);
                });
    }

    private CartDTO mapToCartDTO(Cart cart) {
        List<CartItemDTO> items = cart.getItems().stream()
                .map(this::mapToCartItemDTO)
                .toList();

        return CartDTO.builder()
                .id(cart.getId())
                .userId(cart.getUser().getId())
                .items(items)
                .totalPrice(cart.getTotalPrice())
                .totalItems(cart.getTotalItems())
                .createdAt(cart.getCreatedAt())
                .updatedAt(cart.getUpdatedAt())
                .build();
    }

    private CartItemDTO mapToCartItemDTO(CartItem item) {
        ProductSku sku = item.getProductSku();

        return CartItemDTO.builder()
                .id(item.getId())
                .productId(sku.getProduct().getId())
                .productName(sku.getProduct().getName())
                .productImage(sku.getProduct().getCoverImage())
                .skuId(sku.getId())
                .skuCode(sku.getSku())
                .quantity(item.getQuantity())
                .price(item.getPrice())
                .currentPrice(sku.getPrice())
                .subtotal(item.getSubtotal())
                .inStock(sku.getQuantity() >= item.getQuantity())
                .availableStock(sku.getQuantity())
                .build();
    }
}
