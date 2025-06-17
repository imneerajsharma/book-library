package com.neeraj.book_library.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.neeraj.book_library.dto.BookPageResponse;
import com.neeraj.book_library.dto.BookRequestDTO;
import com.neeraj.book_library.dto.BookResponseDTO;
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
import java.util.Collections;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests for {@link BookController} using real HTTP layer and mocked service layer.
 */
@WebMvcTest(BookController.class)
@Import(BookControllerTest.MockConfig.class)
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookService bookService;

    @Autowired
    private ObjectMapper objectMapper;

    private BookRequestDTO bookRequest;
    private BookResponseDTO bookResponse;

    @BeforeEach
    void setUp() {
        bookRequest = BookRequestDTO.builder()
                .title("Effective Java")
                .author("Joshua Bloch")
                .description("A guide to Java best practices")
                .isbn("9780134685991")
                .publishedDate(LocalDate.of(2018, 5, 10))
                .build();

        bookResponse = BookResponseDTO.builder()
                .id("1")
                .title("Effective Java")
                .author("Joshua Bloch")
                .description("A guide to Java best practices")
                .isbn("9780134685991")
                .publishedDate(LocalDate.of(2018, 5, 10))
                .build();
    }

    @Test
    void testCreateBook() throws Exception {
        when(bookService.createBook(any(BookRequestDTO.class))).thenReturn(bookResponse);

        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Effective Java"))
                .andExpect(jsonPath("$.author").value("Joshua Bloch"));
    }

    @Test
    void testCreateBooksBulk() throws Exception {
        when(bookService.createBooksBulk(anyList())).thenReturn(Collections.singletonList(bookResponse));

        mockMvc.perform(post("/api/books/bulk")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Collections.singletonList(bookRequest))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].isbn").value("9780134685991"));
    }

    @Test
    void testGetBookById() throws Exception {
        when(bookService.getBookById("1")).thenReturn(bookResponse);

        mockMvc.perform(get("/api/books/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Effective Java"));
    }

    @Test
    void testGetAllBooks() throws Exception {
        BookPageResponse response = new BookPageResponse(
                Collections.singletonList(bookResponse), 1L, 1, 0, 10);
        when(bookService.getAllBooks(0, 10)).thenReturn(response);

        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.books[0].title").value("Effective Java"))
                .andExpect(jsonPath("$.data.totalElements").value(1));
    }

    @Test
    void testUpdateBook() throws Exception {
        when(bookService.updateBook(eq("1"), any(BookRequestDTO.class))).thenReturn(bookResponse);

        mockMvc.perform(put("/api/books/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isbn").value("9780134685991"));
    }

    @Test
    void testDeleteBook() throws Exception {
        mockMvc.perform(delete("/api/books/1"))
                .andExpect(status().isNoContent());
        verify(bookService).deleteBook("1");
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
