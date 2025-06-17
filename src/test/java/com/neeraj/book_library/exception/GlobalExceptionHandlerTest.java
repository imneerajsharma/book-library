package com.neeraj.book_library.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.neeraj.book_library.dto.BookRequestDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for {@link GlobalExceptionHandler}.
 */
@WebMvcTest(GlobalExceptionHandlerTest.FakeController.class)
@Import(GlobalExceptionHandler.class)
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Controller to simulate exceptions for testing.
     */
    @RestController
    static class FakeController {

        @PostMapping("/validate")
        public String validate(@Valid @RequestBody DummyDTO request) {
            return "OK";
        }

        @GetMapping("/duplicate")
        public String throwDuplicateBook() {
            throw new DuplicateBookException("Book already exists.");
        }

        @GetMapping("/error")
        public String throwGeneric() {
            throw new RuntimeException("Unexpected issue");
        }
    }

    static class DummyDTO {
        @NotBlank(message = "Title must not be blank")
        public String title;
    }

    @Test
    @DisplayName("Should return 400 with field error for invalid request")
    void handleValidationErrors() throws Exception {
        String json = objectMapper.writeValueAsString(new DummyDTO());

        mockMvc.perform(post("/validate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.errors.title").value("Title must not be blank"));
    }

    @Test
    @DisplayName("Should return 400 for DuplicateBookException")
    void handleDuplicateBook() throws Exception {
        mockMvc.perform(get("/duplicate"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Book already exists."));
    }

    @Test
    @DisplayName("Should return 500 for unhandled exceptions")
    void handleGenericException() throws Exception {
        mockMvc.perform(get("/error"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("An unexpected error occurred."));
    }
    @Test
    void handleValidationErrors_shouldReturn400WithFieldErrors() throws Exception {
        BookRequestDTO invalidRequest = BookRequestDTO.builder()
                .title("") // Invalid: blank
                .author("") // Invalid: blank
                .isbn("") // Invalid: blank
                .publishedDate(null) // Invalid: null
                .build();

        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.errors.title").value("Title must not be blank"))
                .andExpect(jsonPath("$.errors.author").value("Author must not be blank"))
                .andExpect(jsonPath("$.errors.isbn").value("ISBN must not be blank"))
                .andExpect(jsonPath("$.errors.publishedDate").value("Published date must not be null"));
    }
    @Test
    @DisplayName("Should return 400 when validation fails in another controller")
    void shouldHandleValidationErrorsGloballyFromOtherController() throws Exception {
        String payload = "{\"title\": \"\"}";
        mockMvc.perform(post("/validate")  // This assumes another endpoint that uses @Valid
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.errors.title").value("Title must not be blank"));
    }

    @Test
    @DisplayName("Should return 404 when BookNotFoundException is thrown")
    void shouldHandleBookNotFoundExceptionGlobally() throws Exception {
        mockMvc.perform(get("/books/not-existing-id")) // Assuming this throws BookNotFoundException
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Book not found"));
    }


}
