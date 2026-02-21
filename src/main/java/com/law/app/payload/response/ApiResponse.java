package com.law.app.payload.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiResponse<T> {
    @Builder.Default
    private Instant timestamp = Instant.now();
    private int status;
    private boolean success;
    private String message;
    private T data;

    public static <T> ApiResponse<T> ok(String message, T data) {
        return ApiResponse.<T>builder()
            .status(HttpStatus.OK.value())
            .success(true)
            .message(message)
            .data(data)
            .build();
    }

    public static <T> ApiResponse<T> error(String message, T data) {
        return error(HttpStatus.BAD_REQUEST, message, data);
    }

    public static <T> ApiResponse<T> error(HttpStatus status, String message, T data) {
        return ApiResponse.<T>builder()
            .status(status.value())
            .success(false)
            .message(message)
            .data(data)
            .build();
    }

    public static <T> ResponseEntity<ApiResponse<T>> okEntity(String message, T data) {
        return ResponseEntity.ok(ok(message, data));
    }

    public static <T> ResponseEntity<ApiResponse<T>> errorEntity(HttpStatus status, String message, T data) {
        return ResponseEntity.status(status).body(error(status, message, data));
    }
}
