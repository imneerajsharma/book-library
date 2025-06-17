package com.neeraj.book_library.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.neeraj.book_library.dto.BookRequestDTO;
import com.neeraj.book_library.exception.BookNotFoundException;
import com.neeraj.book_library.exception.DuplicateBookException;
import com.neeraj.book_library.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
@Import(BookControllerEdgeCaseTests.MockConfig.class)
class BookControllerEdgeCaseTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookService bookService;

    @Autowired
    private ObjectMapper objectMapper;

    private BookRequestDTO invalidRequest;

    @BeforeEach
    void setup() {
        invalidRequest = BookRequestDTO.builder()
                .title("")
                .author("")
                .isbn("")
                .description("Missing fields")
                .publishedDate(null)
                .build();
    }

    @Test
    void testCreateBookValidationFailure() throws Exception {
        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Title must not be blank"))
                .andExpect(jsonPath("$.author").value("Author must not be blank"))
                .andExpect(jsonPath("$.isbn").value("ISBN must not be blank"));
    }

    @Test
    void testGetBookByInvalidId() throws Exception {
        when(bookService.getBookById("invalid-id")).thenThrow(new BookNotFoundException("Book not found"));

        mockMvc.perform(get("/api/books/invalid-id"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Book not found"));
    }

    @Test
    void testCreateBookWithDuplicateISBN() throws Exception {
        BookRequestDTO duplicateBook = BookRequestDTO.builder()
                .title("Some Title")
                .author("Some Author")
                .isbn("9781234567890")
                .description("Desc")
                .publishedDate(LocalDate.of(2020, 1, 1))
                .build();

        doThrow(new DuplicateBookException("Duplicate ISBN"))
                .when(bookService).createBook(any(BookRequestDTO.class));

        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(duplicateBook)))
                .andExpect(status().isConflict())
                .andExpect(content().string("Duplicate ISBN"));
    }

    @TestConfiguration
    static class MockConfig {
        @Bean
        public BookService bookService() {
            return mock(BookService.class);
        }

        @Bean
        public ModelMapper modelMapper() {
            return new ModelMapper();
        }
    }
}
