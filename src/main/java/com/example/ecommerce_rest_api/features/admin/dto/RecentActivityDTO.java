package com.example.ecommerce_rest_api.features.admin.dto;

import lombok.*;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecentActivityDTO {
    private List<ActivityItemDTO> activities;
}
