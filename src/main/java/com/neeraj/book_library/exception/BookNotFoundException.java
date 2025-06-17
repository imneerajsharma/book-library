package com.neeraj.book_library.exception;

/**
 * Exception thrown when a requested book is not found in the database.
 */
public class BookNotFoundException extends RuntimeException {

    /**
     * Constructs a new BookNotFoundException with the specified detail message.
     *
     * @param message the detail message
     */
    public BookNotFoundException(String message) {
        super(message);
    }

    /**
     * Constructs a new BookNotFoundException with the specified message and cause.
     *
     * @param message the detail message
     * @param cause   the cause (can be retrieved later via getCause())
     */
    public BookNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
