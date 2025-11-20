package com.example.ecommerce_rest_api.common.DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class ErrorDetailDTO {
    private LocalDateTime timeStamp;
    private HttpStatus status;
    private String message;
    private String details;
}
