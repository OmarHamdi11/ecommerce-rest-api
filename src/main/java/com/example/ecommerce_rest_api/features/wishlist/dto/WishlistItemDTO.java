package com.example.ecommerce_rest_api.features.wishlist.dto;

import lombok.*;
import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WishlistItemDTO {
    private Long id;
    private Long productId;
    private String productName;
    private String productSlug;
    private String productImage;
    private Double price;
    private Boolean isAvailable;
    private LocalDateTime addedAt;
}
