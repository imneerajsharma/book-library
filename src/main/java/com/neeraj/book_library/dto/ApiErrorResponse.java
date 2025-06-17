package com.neeraj.book_library.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.ZonedDateTime;

/**
 * Standardized API error response structure.
 */
@Data
@Builder
@Schema(description = "Standard structure for API error responses")
public class ApiErrorResponse {

    @Schema(description = "Timestamp of when the error occurred", example = "2025-06-17 14:32:10 UTC")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss z")
    private final ZonedDateTime timestamp;

    @Schema(description = "HTTP status code", example = "404")
    private final int status;

    @Schema(description = "Short error name or label", example = "Not Found")
    private final String error;

    @Schema(description = "Detailed error message", example = "Book with ID 123 not found")
    private final String message;

    @Schema(description = "Request path that caused the error", example = "/api/books/123")
    private final String path;
}
