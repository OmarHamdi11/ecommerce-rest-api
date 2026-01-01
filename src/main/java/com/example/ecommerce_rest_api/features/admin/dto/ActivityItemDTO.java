package com.example.ecommerce_rest_api.features.admin.dto;

import lombok.*;
import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ActivityItemDTO {
    private String type;
    private String description;
    private LocalDateTime timestamp;
}
