package com.example.ecommerce_rest_api.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseApi<T>{

    private boolean success;
    private int status;
    private String message;
    private LocalDateTime timestamp;
    private T data;
    private String path;

    public ResponseApi(int status, String message, T data) {
        this.success = status >= 200 && status < 300;
        this.status = status;
        this.message = message;
        this.timestamp = LocalDateTime.now();
        this.data = data;
    }

    public ResponseApi(int status, String message) {
        this.success = false;
        this.status = status;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

    public static <T> ResponseApi<T> success(String message, T data) {
        return new ResponseApi<>(200, message, data);
    }

    public static <T> ResponseApi<T> created(String message, T data) {
        return new ResponseApi<>(201, message, data);
    }

    public static <T> ResponseApi<T> error(int status, String message) {
        return new ResponseApi<>(status, message);
    }

}