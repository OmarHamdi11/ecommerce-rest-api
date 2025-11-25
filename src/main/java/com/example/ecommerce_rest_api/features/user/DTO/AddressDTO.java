package com.example.ecommerce_rest_api.features.user.DTO;


import com.example.ecommerce_rest_api.features.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddressDTO {
    private long id;
    private String title;
    private String address_line_1;
    private String address_line_2;
    private String country;
    private String city;
    private String postal_code;
    private String landmark;
    private String phone_number;
}
