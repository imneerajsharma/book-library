package com.neeraj.book_library.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.time.Instant;

/**
 * Generic wrapper for API responses.
 *
 * @param <T> the type of the actual response payload
 */
@Data
@Builder
@AllArgsConstructor
@Schema(description = "Standard response wrapper for all API responses")
public class ApiResponseWrapper<T> {

    @Schema(description = "Indicates whether the request was successful", example = "true")
    private boolean success;

    @Schema(description = "Human-readable message describing the result", example = "Book created successfully")
    private String message;

    @Schema(description = "The actual response data", example = "...")
    private T data;

    @Schema(description = "Timestamp of the response generation", example = "2024-06-17T15:00:00Z")
    private Instant timestamp;

    public static <T> ApiResponseWrapper<T> success(String message, T data) {
        return new ApiResponseWrapper<>(true, message, data, Instant.now());
    }

    public static <T> ApiResponseWrapper<T> failure(String message, T data) {
        return new ApiResponseWrapper<>(false, message, data, Instant.now());
    }
}
