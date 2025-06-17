package com.neeraj.book_library.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;

import java.time.ZonedDateTime;

/**
 * Standardized API error response structure.
 */
@Getter
@Builder
public class ApiErrorResponse {

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss z")
    private final ZonedDateTime timestamp;

    private final int status;

    private final String error;

    private final String message;

    private final String path;
}
