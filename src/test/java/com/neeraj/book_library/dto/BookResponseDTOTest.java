package com.neeraj.book_library.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link BookResponseDTO}.
 * Ensures correct serialization/deserialization and field mapping.
 */
class BookResponseDTOTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void shouldSerializeAndDeserializeCorrectly() throws Exception {
        BookResponseDTO original = BookResponseDTO.builder()
                .id("abc123")
                .title("Domain-Driven Design")
                .author("Eric Evans")
                .description("Tackling complexity in the heart of software")
                .isbn("9780321125217")
                .publishedDate(LocalDate.of(2003, 8, 30))
                .build();

        String json = objectMapper.writeValueAsString(original);
        BookResponseDTO deserialized = objectMapper.readValue(json, BookResponseDTO.class);

        assertEquals(original.getId(), deserialized.getId());
        assertEquals(original.getTitle(), deserialized.getTitle());
        assertEquals(original.getAuthor(), deserialized.getAuthor());
        assertEquals(original.getDescription(), deserialized.getDescription());
        assertEquals(original.getIsbn(), deserialized.getIsbn());
        assertEquals(original.getPublishedDate(), deserialized.getPublishedDate());
    }

    @Test
    void shouldHandleNullFieldsGracefully() throws Exception {
        BookResponseDTO dto = BookResponseDTO.builder()
                .id(null)
                .title(null)
                .author(null)
                .description(null)
                .isbn(null)
                .publishedDate(null)
                .build();

        String json = objectMapper.writeValueAsString(dto);
        BookResponseDTO deserialized = objectMapper.readValue(json, BookResponseDTO.class);

        assertNull(deserialized.getId());
        assertNull(deserialized.getTitle());
        assertNull(deserialized.getAuthor());
        assertNull(deserialized.getDescription());
        assertNull(deserialized.getIsbn());
        assertNull(deserialized.getPublishedDate());
    }
    @Test
    void shouldFailDeserializationOnInvalidDateFormat() {
        String invalidJson = """
            {
                "id": "abc123",
                "title": "Book",
                "author": "Author",
                "description": "Desc",
                "isbn": "1234567890",
                "publishedDate": "08-30-2003"
            }
        """;

        assertThrows(Exception.class, () ->
                objectMapper.readValue(invalidJson, BookResponseDTO.class));
    }

    @Test
    void shouldSerializeDateInExpectedFormat() throws Exception {
        BookResponseDTO dto = BookResponseDTO.builder()
                .publishedDate(LocalDate.of(2023, 12, 25))
                .build();

        String json = objectMapper.writeValueAsString(dto);
        assertTrue(json.contains("\"publishedDate\":\"2023-12-25\""));
    }

    @Test
    void shouldSerializeSpecialCharactersCorrectly() throws Exception {
        BookResponseDTO dto = BookResponseDTO.builder()
                .description("This \"quote\" should be escaped")
                .build();

        String json = objectMapper.writeValueAsString(dto);
        assertTrue(json.contains("This \\\"quote\\\""));
    }

}
