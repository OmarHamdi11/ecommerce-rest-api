package com.example.ecommerce_rest_api.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class TokenException extends RuntimeException {
    private final HttpStatus status;
    private final String message;

    public TokenException(String message, HttpStatus status, String message1) {
        super(message);
        this.status = status;
        this.message = message1;
    }
    public TokenException(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
