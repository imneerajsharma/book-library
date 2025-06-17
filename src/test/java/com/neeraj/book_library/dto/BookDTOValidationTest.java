package com.neeraj.book_library.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests to validate BookRequestDTO constraints using Jakarta Bean Validation.
 */
class BookDTOValidationTest {

    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void validBookDTO_ShouldPassValidation() {
        BookRequestDTO dto = BookRequestDTO.builder()
                .title("Clean Code")
                .author("Robert C. Martin")
                .isbn("9780132350884")
                .description("A handbook of agile software craftsmanship")
                .publishedDate(LocalDate.of(2008, 8, 1))
                .build();

        Set<ConstraintViolation<BookRequestDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty(), "Expected no validation errors for a valid DTO");
    }

    @Test
    void blankTitle_ShouldFailValidation() {
        BookRequestDTO dto = BookRequestDTO.builder()
                .title("")
                .author("Author")
                .isbn("1234567890")
                .description("Test book")
                .publishedDate(LocalDate.now())
                .build();

        Set<ConstraintViolation<BookRequestDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("title")));
    }

    @Test
    void nullPublishedDate_ShouldFailValidation() {
        BookRequestDTO dto = BookRequestDTO.builder()
                .title("Test Book")
                .author("Author")
                .isbn("1234567890")
                .description("Test book")
                .publishedDate(null)
                .build();

        Set<ConstraintViolation<BookRequestDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("publishedDate")));
    }

    @Test
    void blankAuthor_ShouldFailValidation() {
        BookRequestDTO dto = BookRequestDTO.builder()
                .title("Sample Book")
                .author("")
                .isbn("9781234567890")
                .description("Testing blank author")
                .publishedDate(LocalDate.now())
                .build();

        Set<ConstraintViolation<BookRequestDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("author")));
    }

    @Test
    void blankIsbn_ShouldFailValidation() {
        BookRequestDTO dto = BookRequestDTO.builder()
                .title("Sample Book")
                .author("Author Name")
                .isbn("")
                .description("Testing blank ISBN")
                .publishedDate(LocalDate.now())
                .build();

        Set<ConstraintViolation<BookRequestDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("isbn")));
    }

    @Test
    void invalidIsbnPattern_ShouldFailValidation() {
        BookRequestDTO dto = BookRequestDTO.builder()
                .title("Sample Book")
                .author("Author Name")
                .isbn("INVALID_ISBN")
                .description("Testing malformed ISBN")
                .publishedDate(LocalDate.now())
                .build();

        Set<ConstraintViolation<BookRequestDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("isbn")));
    }

    @Test
    void futurePublishedDate_ShouldFailValidation() {
        BookRequestDTO dto = BookRequestDTO.builder()
                .title("Sample Book")
                .author("Author Name")
                .isbn("9781234567890")
                .description("Testing future publishedDate")
                .publishedDate(LocalDate.now().plusDays(10))
                .build();

        Set<ConstraintViolation<BookRequestDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("publishedDate")));
    }
}
