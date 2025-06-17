package com.neeraj.book_library.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class BookPageResponseTest {

    @Test
    void testNoArgsConstructorAndSetters() {
        BookResponseDTO book1 = BookResponseDTO.builder()
                .id("1")
                .title("Title 1")
                .author("Author 1")
                .isbn("ISBN1")
                .description("Desc 1")
                .publishedDate(LocalDate.of(2024, 1, 1))
                .build();

        BookPageResponse response = new BookPageResponse();
        response.setBooks(List.of(book1));
        response.setPage(0);
        response.setSize(10);
        response.setTotalElements(1L);
        response.setTotalPages(1);

        assertThat(response.getBooks()).containsExactly(book1);
        assertThat(response.getPage()).isEqualTo(0);
        assertThat(response.getSize()).isEqualTo(10);
        assertThat(response.getTotalElements()).isEqualTo(1L);
        assertThat(response.getTotalPages()).isEqualTo(1);
    }

    @Test
    void testAllArgsConstructor() {
        BookResponseDTO book = BookResponseDTO.builder()
                .id("2")
                .title("Book 2")
                .author("Author 2")
                .isbn("ISBN2")
                .description("Description 2")
                .publishedDate(LocalDate.of(2023, 12, 12))
                .build();

        BookPageResponse response = new BookPageResponse(
                List.of(book), 5L, 1, 0, 10
        );

        assertThat(response.getBooks()).hasSize(1);
        assertThat(response.getTotalElements()).isEqualTo(5L);
        assertThat(response.getTotalPages()).isEqualTo(1);
        assertThat(response.getPage()).isEqualTo(0);
        assertThat(response.getSize()).isEqualTo(10);
    }

    @Test
    void testBuilder() {
        BookResponseDTO book = BookResponseDTO.builder()
                .id("3")
                .title("Builder Book")
                .author("Builder Author")
                .isbn("ISBN3")
                .description("Builder Description")
                .publishedDate(LocalDate.of(2023, 11, 11))
                .build();

        BookPageResponse response = BookPageResponse.builder()
                .books(List.of(book))
                .page(1)
                .size(5)
                .totalElements(20L)
                .totalPages(4)
                .build();

        assertThat(response.getBooks()).contains(book);
        assertThat(response.getPage()).isEqualTo(1);
        assertThat(response.getSize()).isEqualTo(5);
        assertThat(response.getTotalElements()).isEqualTo(20L);
        assertThat(response.getTotalPages()).isEqualTo(4);
    }

    @Test
    void testSerialization() throws JsonProcessingException {
        BookResponseDTO book = BookResponseDTO.builder()
                .id("4")
                .title("Serialized Book")
                .author("Ser Author")
                .isbn("ISBN4")
                .description("Serialized Description")
                .publishedDate(LocalDate.of(2022, 10, 10))
                .build();

        BookPageResponse response = BookPageResponse.builder()
                .books(List.of(book))
                .page(2)
                .size(15)
                .totalElements(30L)
                .totalPages(2)
                .build();

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(response);

        assertThat(json).contains("Serialized Book").contains("page").contains("totalElements");

        BookPageResponse deserialized = mapper.readValue(json, BookPageResponse.class);
        assertThat(deserialized).isEqualTo(response);
    }

    @Test
    void testToStringAndEquality() {
        BookResponseDTO book = BookResponseDTO.builder()
                .id("5")
                .title("Equal Book")
                .author("Equal Author")
                .isbn("ISBN5")
                .description("Equality Description")
                .publishedDate(LocalDate.of(2022, 1, 1))
                .build();

        BookPageResponse response1 = BookPageResponse.builder()
                .books(List.of(book))
                .page(0)
                .size(1)
                .totalElements(1L)
                .totalPages(1)
                .build();

        BookPageResponse response2 = BookPageResponse.builder()
                .books(List.of(book))
                .page(0)
                .size(1)
                .totalElements(1L)
                .totalPages(1)
                .build();

        assertThat(response1).isEqualTo(response2);
        assertThat(response1.toString()).contains("Equal Book");
    }

    @Test
    void testEmptyBooksList() {
        BookPageResponse response = BookPageResponse.builder()
                .books(Collections.emptyList())
                .page(0)
                .size(10)
                .totalElements(0L)
                .totalPages(0)
                .build();

        assertThat(response.getBooks()).isEmpty();
        assertThat(response.getTotalElements()).isZero();
    }

    @Test
    void testNullFields() {
        BookPageResponse response = new BookPageResponse();
        assertThat(response.getBooks()).isNull();
        assertThat(response.getPage()).isZero();
        assertThat(response.getSize()).isZero();
        assertThat(response.getTotalElements()).isZero();
        assertThat(response.getTotalPages()).isZero();
    }
}
