package com.neeraj.book_library.exception;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DuplicateBookExceptionTest {

    @Test
    void testExceptionMessage() {
        String message = "Book with ISBN 1234567890 already exists.";
        DuplicateBookException exception = new DuplicateBookException(message);

        assertThat(exception).isInstanceOf(DuplicateBookException.class);
        assertThat(exception.getMessage()).isEqualTo(message);
    }

    @Test
    void testThrowingDuplicateBookException() {
        String isbn = "1234567890";
        Exception exception = assertThrows(DuplicateBookException.class, () -> {
            throw new DuplicateBookException("Book with ISBN " + isbn + " already exists.");
        });

        assertThat(exception.getMessage()).contains(isbn);
    }

    @Test
    void testToStringAndEquality() {
        DuplicateBookException ex1 = new DuplicateBookException("Conflict");
        DuplicateBookException ex2 = new DuplicateBookException("Conflict");

        assertThat(ex1.getMessage()).isEqualTo("Conflict");
        assertThat(ex2.getMessage()).isEqualTo("Conflict");
        assertThat(ex1).isNotSameAs(ex2);
    }

    @Test
    void testNullMessageDoesNotThrow() {
        DuplicateBookException exception = new DuplicateBookException(null);
        assertThat(exception.getMessage()).isNull();
    }

}
