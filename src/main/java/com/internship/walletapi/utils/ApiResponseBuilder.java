package com.internship.walletapi.utils;

import com.internship.walletapi.payload.ApiResponse;
import org.springframework.http.ResponseEntity;

public class ApiResponseBuilder {
    public static <T> ResponseEntity<Object> buildErrorResponseEntity(ApiResponse<?> response) {
        return new ResponseEntity<>(response, response.getStatus());
    }

    public static <T> ResponseEntity<ApiResponse<T>> buildResponseEntity(ApiResponse<T> response) {
        return new ResponseEntity<>(response, response.getStatus());
    }
}
