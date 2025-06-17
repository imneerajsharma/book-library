package com.neeraj.book_library.exception;

import com.neeraj.book_library.dto.ApiErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Global exception handler for standardized API error responses.
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BookNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleBookNotFound(BookNotFoundException ex, HttpServletRequest request) {
        log.warn("Book not found: {}", ex.getMessage());
        return buildErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND, request.getRequestURI());
    }

    @ExceptionHandler(DuplicateBookException.class)
    public ResponseEntity<ApiErrorResponse> handleDuplicateBook(DuplicateBookException ex, HttpServletRequest request) {
        log.warn("Duplicate book error: {}", ex.getMessage());
        return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST, request.getRequestURI());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex, HttpServletRequest request) {
        String errorMessages = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining("; "));
        log.warn("Validation error(s): {}", errorMessages);
        return buildErrorResponse("Validation failed: " + errorMessages, HttpStatus.BAD_REQUEST, request.getRequestURI());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGenericException(Exception ex, HttpServletRequest request) {
        log.error("Unhandled exception occurred", ex);
        return buildErrorResponse("An unexpected error occurred.", HttpStatus.INTERNAL_SERVER_ERROR, request.getRequestURI());
    }

    private ResponseEntity<ApiErrorResponse> buildErrorResponse(String message, HttpStatus status, String path) {
        ApiErrorResponse response = ApiErrorResponse.builder()
                .timestamp(ZonedDateTime.now())
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(message)
                .path(path)
                .build();
        return ResponseEntity.status(status).body(response);
    }
}


//package com.neeraj.book_library.exception;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.MethodArgumentNotValidException;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * Global exception handler for centralized error handling across controllers.
// */
//@Slf4j
//@ControllerAdvice
//public class GlobalExceptionHandler {
//
//    /**
//     * Handles BookNotFoundException and returns a 404 response.
//     *
//     * @param ex the exception thrown
//     * @return 404 response with error message
//     */
//    @ExceptionHandler(BookNotFoundException.class)
//    public ResponseEntity<String> handleBookNotFound(BookNotFoundException ex) {
//        log.warn("Book not found: {}", ex.getMessage());
//        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
//    }
//
//    /**
//     * Handles validation errors for invalid request bodies and returns a 400 response.
//     *
//     * @param ex validation exception
//     * @return 400 response with field-wise error messages
//     */
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<Map<String, String>> handleValidationErrors(MethodArgumentNotValidException ex) {
//        Map<String, String> errors = new HashMap<>();
//        ex.getBindingResult().getFieldErrors().forEach(error ->
//                errors.put(error.getField(), error.getDefaultMessage()));
//        log.warn("Validation error(s): {}", errors);
//        return ResponseEntity.badRequest().body(errors);
//    }
//
//    /**
//     * Handles DuplicateBookException and returns a 400 response.
//     *
//     * @param ex the exception thrown
//     * @return 400 response with error message
//     */
//    @ExceptionHandler(DuplicateBookException.class)
//    public ResponseEntity<String> handleDuplicateBook(DuplicateBookException ex) {
//        log.warn("Duplicate book error: {}", ex.getMessage());
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
//    }
//
//    /**
//     * Fallback handler for any unhandled exceptions, returns a 500 response.
//     *
//     * @param ex the exception thrown
//     * @return 500 response with generic error message
//     */
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<String> handleGenericException(Exception ex) {
//        log.error("Unhandled exception occurred", ex);
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                .body("An unexpected error occurred.");
//    }
//}
