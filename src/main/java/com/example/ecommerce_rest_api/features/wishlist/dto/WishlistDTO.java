package com.example.ecommerce_rest_api.features.wishlist.dto;

import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WishlistDTO {
    private Long id;
    private Long userId;
    private List<WishlistItemDTO> items;
    private Integer totalItems;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}