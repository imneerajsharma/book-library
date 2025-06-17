package com.neeraj.book_library.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.neeraj.book_library.dto.BookRequestDTO;
import com.neeraj.book_library.dto.BookResponseDTO;
import com.neeraj.book_library.repository.BookRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BookControllerIntegrationTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private BookRepository bookRepository;

    private String testBookId;

    @BeforeEach
    void setupTestBook() throws Exception {
        BookRequestDTO bookRequest = createSampleBook("Test Driven Development", "9780321146533");
        testBookId = createBookAndReturnId(bookRequest);
    }

    @AfterEach
    void cleanup() {
        bookRepository.deleteAll();
    }

    // Helper: Create a sample book DTO
    private BookRequestDTO createSampleBook(String title, String isbn) {
        return BookRequestDTO.builder()
                .title(title)
                .author("Kent Beck")
                .isbn(isbn)
                .description("Sample description")
                .publishedDate(LocalDate.of(2002, 5, 1))
                .build();
    }

    // Helper: Create book and return generated ID
    private String createBookAndReturnId(BookRequestDTO request) throws Exception {
        String response = mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        return objectMapper.readValue(response, BookResponseDTO.class).getId();
    }

    @Test
    @DisplayName("Should fetch book by ID")
    void shouldReturnBookById() throws Exception {
        mockMvc.perform(get("/api/books/{id}", testBookId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(testBookId)))
                .andExpect(jsonPath("$.title", is("Test Driven Development")));
    }

    @Test
    @DisplayName("Should return all books with pagination")
    void shouldReturnAllBooks() throws Exception {
        mockMvc.perform(get("/api/books?page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.books", not(empty())))
                .andExpect(jsonPath("$.totalElements", greaterThanOrEqualTo(1)));
    }

    @Test
    @DisplayName("Should update book by ID")
    void shouldUpdateBook() throws Exception {
        BookRequestDTO updatedRequest = createSampleBook("Refactoring", "9780201485677");

        mockMvc.perform(put("/api/books/{id}", testBookId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Refactoring")))
                .andExpect(jsonPath("$.author", is("Kent Beck"))); // Author stays same here
    }

    @Test
    @DisplayName("Should delete book by ID")
    void shouldDeleteBookSuccessfully() throws Exception {
        mockMvc.perform(delete("/api/books/{id}", testBookId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString("Book deleted successfully")))
                .andExpect(jsonPath("$.deletedBookId", is(testBookId)));
    }

    @Test
    @DisplayName("Should return 404 when deleting non-existent book")
    void shouldReturn404WhenDeletingNonexistentBook() throws Exception {
        mockMvc.perform(delete("/api/books/{id}", "nonexistent-id"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("Book not found")));
    }

    @Test
    @DisplayName("Should return 400 for invalid input on POST")
    void shouldReturn400ForInvalidBookCreate() throws Exception {
        BookRequestDTO invalidBook = BookRequestDTO.builder()
                .title("")  // Invalid: title blank
                .author("Some Author")
                .isbn("")   // Invalid: ISBN blank
                .description("Invalid data")
                .publishedDate(LocalDate.now())
                .build();

        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidBook)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title", notNullValue()))
                .andExpect(jsonPath("$.isbn", notNullValue()));
    }

    @Test
    @DisplayName("Should return 409 when creating duplicate ISBN")
    void shouldReturn409ForDuplicateIsbn() throws Exception {
        BookRequestDTO duplicateBook = createSampleBook("Another Book", "9780321146533");

        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(duplicateBook)))
                .andExpect(status().isConflict())
                .andExpect(content().string(containsString("already exists")));
    }

    @Test
    @DisplayName("Should return 404 when updating non-existent book")
    void shouldReturn404ForUpdateOfNonexistentBook() throws Exception {
        BookRequestDTO updatedRequest = createSampleBook("Clean Code", "9780132350884");

        mockMvc.perform(put("/api/books/{id}", "nonexistent-id")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedRequest)))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("Book not found")));
    }
}
