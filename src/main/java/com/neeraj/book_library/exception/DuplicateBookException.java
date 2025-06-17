package com.neeraj.book_library.exception;

/**
 * Exception thrown when attempting to add a book that already exists (based on ISBN).
 */
public class DuplicateBookException extends RuntimeException {

    /**
     * Constructs a new DuplicateBookException with the specified detail message.
     *
     * @param message the detail message
     */
    public DuplicateBookException(String message) {
        super(message);
    }
}
