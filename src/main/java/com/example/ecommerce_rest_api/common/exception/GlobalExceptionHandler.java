package com.example.ecommerce_rest_api.common.exception;

import com.example.ecommerce_rest_api.common.DTOs.ErrorDetailDTO;
import com.example.ecommerce_rest_api.common.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(TokenException.class)
    public ResponseEntity<ApiResponse<Object>> handleTokenException(TokenException exception,
                                                                    WebRequest request
    ) {
        ApiResponse<Object> response = ApiResponse.error(
                HttpStatus.BAD_REQUEST.value(),
                "An error occurred: " + exception.getMessage()
        );
        response.setPath(request.getDescription(false).replace("uri:",""));
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }


    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleResourceNotFoundException(
            ResourceNotFoundException exception, WebRequest request
    ){
        ApiResponse<Object> response = ApiResponse.error(
                HttpStatus.NOT_FOUND.value(),
                "An error occurred: " + exception.getMessage()
        );
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(response);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetailDTO> handleGlobalException (Exception exception,
                                                                 WebRequest webRequest
    ){
        ErrorDetailDTO errorDetailDTO = new ErrorDetailDTO(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST,
                exception.getMessage(),
                webRequest.getDescription(false)
        );

        return new ResponseEntity<>(errorDetailDTO,HttpStatus.BAD_REQUEST);
    }

}
