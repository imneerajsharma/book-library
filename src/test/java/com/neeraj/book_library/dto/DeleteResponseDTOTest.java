package com.neeraj.book_library.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DeleteResponseDTOTest {

    @Test
    void testNoArgsConstructorAndSetters() {
        DeleteResponseDTO dto = new DeleteResponseDTO();
        dto.setMessage("Deleted");
        dto.setDeletedBookId("book123");

        assertThat(dto.getMessage()).isEqualTo("Deleted");
        assertThat(dto.getDeletedBookId()).isEqualTo("book123");
    }

    @Test
    void testAllArgsConstructor() {
        DeleteResponseDTO dto = new DeleteResponseDTO("Deleted successfully", "abc123", "isbn123", "Test Title");

        assertThat(dto.getMessage()).isEqualTo("Deleted successfully");
        assertThat(dto.getDeletedBookId()).isEqualTo("abc123");
        assertThat(dto.getIsbn()).isEqualTo("isbn123");
        assertThat(dto.getTitle()).isEqualTo("Test Title");
    }

    @Test
    void testBuilder() {
        DeleteResponseDTO dto = DeleteResponseDTO.builder()
                .message("Book deleted")
                .deletedBookId("book999")
                .build();

        assertThat(dto.getMessage()).isEqualTo("Book deleted");
        assertThat(dto.getDeletedBookId()).isEqualTo("book999");
    }

    @Test
    void testEqualsAndHashCode() {
        DeleteResponseDTO dto1 = new DeleteResponseDTO("msg", "id123", "isbnX", "TitleX");
        DeleteResponseDTO dto2 = new DeleteResponseDTO("msg", "id123", "isbnX", "TitleX");

        assertThat(dto1).isEqualTo(dto2);
        assertThat(dto1.hashCode()).isEqualTo(dto2.hashCode());
    }


    @Test
    void testToString() {
        DeleteResponseDTO dto = new DeleteResponseDTO("Deleted", "id456", "isbnZ", "Some Book");

        assertThat(dto.toString()).contains("Deleted").contains("id456").contains("isbnZ").contains("Some Book");
    }

    @Test
    void testSerialization() throws Exception {
        DeleteResponseDTO dto = DeleteResponseDTO.builder()
                .message("Deleted")
                .deletedBookId("id789")
                .build();

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(dto);

        assertThat(json).contains("Deleted").contains("id789");

        DeleteResponseDTO deserialized = mapper.readValue(json, DeleteResponseDTO.class);
        assertThat(deserialized).isEqualTo(dto);
    }

}
