package com.neeraj.book_library.mapper;

import com.neeraj.book_library.dto.BookRequestDTO;
import com.neeraj.book_library.dto.BookResponseDTO;
import com.neeraj.book_library.entity.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link BookMapper}.
 */
class BookMapperTest {

    private BookMapper bookMapper;

    @BeforeEach
    void setUp() {
        bookMapper = new BookMapper();
    }

    @Test
    void shouldMapBookRequestDtoToEntity() {
        BookRequestDTO dto = BookRequestDTO.builder()
                .title("Clean Code")
                .author("Robert C. Martin")
                .isbn("9780132350884")
                .description("A Handbook of Agile Software Craftsmanship")
                .publishedDate(LocalDate.of(2008, 8, 11))
                .build();

        Book book = bookMapper.toEntity(dto);

        assertEquals(dto.getTitle(), book.getTitle());
        assertEquals(dto.getAuthor(), book.getAuthor());
        assertEquals(dto.getIsbn(), book.getIsbn());
        assertEquals(dto.getDescription(), book.getDescription());
        assertEquals(dto.getPublishedDate(), book.getPublishedDate());
    }

    @Test
    void shouldMapEntityToBookResponseDto() {
        Book book = Book.builder()
                .id("abc123")
                .title("Refactoring")
                .author("Martin Fowler")
                .isbn("9780201485677")
                .description("Improving the Design of Existing Code")
                .publishedDate(LocalDate.of(1999, 7, 8))
                .build();

        BookResponseDTO dto = bookMapper.toResponseDTO(book);

        assertEquals(book.getId(), dto.getId());
        assertEquals(book.getTitle(), dto.getTitle());
        assertEquals(book.getAuthor(), dto.getAuthor());
        assertEquals(book.getIsbn(), dto.getIsbn());
        assertEquals(book.getDescription(), dto.getDescription());
        assertEquals(book.getPublishedDate(), dto.getPublishedDate());
    }

    @Test
    void shouldUpdateEntityFromRequestWhilePreservingIdAndIsbn() {
        Book existing = Book.builder()
                .id("book123")
                .title("Old Title")
                .author("Old Author")
                .isbn("LOCKED-ISBN")
                .description("Old Description")
                .publishedDate(LocalDate.of(2020, 1, 1))
                .build();

        BookRequestDTO request = BookRequestDTO.builder()
                .title("New Title")
                .author("New Author")
                .isbn("IGNORED-ISBN") // this should be ignored
                .description("Updated Description")
                .publishedDate(LocalDate.of(2023, 3, 3))
                .build();

        Book updated = bookMapper.updateEntityFromRequest(request, existing);

        assertEquals(existing.getId(), updated.getId());
        assertEquals(existing.getIsbn(), updated.getIsbn()); // should preserve old ISBN
        assertEquals(request.getTitle(), updated.getTitle());
        assertEquals(request.getAuthor(), updated.getAuthor());
        assertEquals(request.getDescription(), updated.getDescription());
        assertEquals(request.getPublishedDate(), updated.getPublishedDate());
    }
    @Test
    void shouldReturnNullWhenRequestIsNullInToEntity() {
        assertNull(bookMapper.toEntity(null));
    }

    @Test
    void shouldReturnNullWhenEntityIsNullInToResponseDTO() {
        assertNull(bookMapper.toResponseDTO(null));
    }

    @Test
    void shouldReturnSameObjectIfUpdateInputIsNull() {
        Book original = Book.builder()
                .id("1")
                .isbn("1234567890")
                .title("Original Title")
                .build();

        Book result = bookMapper.updateEntityFromRequest(null, original);

        assertSame(original, result);
        assertEquals("Original Title", result.getTitle()); // ensures no mutation
    }

}
