package com.neeraj.book_library.exception;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BookNotFoundExceptionTest {

    @Test
    void testExceptionMessage() {
        String message = "Book not found with id: 123";
        BookNotFoundException exception = new BookNotFoundException(message);

        assertThat(exception).isInstanceOf(BookNotFoundException.class);
        assertThat(exception.getMessage()).isEqualTo(message);
    }

    @Test
    void testThrowingBookNotFoundException() {
        String id = "abc123";
        Exception exception = assertThrows(BookNotFoundException.class, () -> {
            throw new BookNotFoundException("Book not found with id: " + id);
        });

        assertThat(exception.getMessage()).contains(id);
    }

    @Test
    void testToStringAndEquality() {
        BookNotFoundException ex1 = new BookNotFoundException("Missing book");
        BookNotFoundException ex2 = new BookNotFoundException("Missing book");

        assertThat(ex1.getMessage()).isEqualTo("Missing book");
        assertThat(ex2.getMessage()).isEqualTo("Missing book");
        assertThat(ex1).isNotSameAs(ex2); // Different instances
    }
    @Test
    void testCustomMessageFormat() {
        String id = "book-456";
        BookNotFoundException exception = new BookNotFoundException("Book not found with id: " + id);
        assertThat(exception.getMessage()).startsWith("Book not found");
    }
}
