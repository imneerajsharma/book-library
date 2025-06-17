package com.neeraj.book_library.service;

import com.neeraj.book_library.dto.BookRequestDTO;
import com.neeraj.book_library.dto.BookResponseDTO;
import com.neeraj.book_library.dto.BookPageResponse;
import com.neeraj.book_library.dto.DeleteResponseDTO;
import com.neeraj.book_library.entity.Book;

import java.util.List;

/**
 * Service interface for managing book operations.
 */
public interface BookService {

    /**
     * Creates a new book from the given request DTO.
     *
     * @param bookRequestDTO the DTO containing book details to create
     * @return the created book as a response DTO
     */
    BookResponseDTO createBook(BookRequestDTO bookRequestDTO);

    /**
     * Creates multiple books in bulk.
     *
     * @param bookRequestDTOs list of book request DTOs
     * @return list of created books as response DTOs
     */
    List<BookResponseDTO> createBooksBulk(List<BookRequestDTO> bookRequestDTOs);

    /**
     * Retrieves a book by its ID.
     *
     * @param id the book ID
     * @return the found book as a response DTO
     * @throws com.neeraj.book_library.exception.BookNotFoundException if the book is not found
     */
    BookResponseDTO getBookById(String id);

    /**
     * Retrieves all books without pagination.
     *
     * @return list of all books as response DTOs
     */
    List<BookResponseDTO> getAllBooksUnpaged();

    /**
     * Retrieves books with pagination.
     *
     * @param page the page number (0-indexed)
     * @param size the number of items per page
     * @return paginated response containing books and metadata
     */
    BookPageResponse getAllBooks(int page, int size);

    /**
     * Updates an existing book by its ID using the provided request DTO.
     *
     * @param id the ID of the book to update
     * @param bookRequestDTO the DTO containing updated book data
     * @return the updated book as a response DTO
     * @throws com.neeraj.book_library.exception.BookNotFoundException if the book is not found
     */
    BookResponseDTO updateBook(String id, BookRequestDTO bookRequestDTO);

    /**
     * Deletes a book by its ID.
     *
     * @param id the ID of the book to delete
     * @return a success message or status (if applicable)
     * @throws com.neeraj.book_library.exception.BookNotFoundException if the book is not found
     */
    DeleteResponseDTO deleteBook(String id);

    /**
     * Saves a book entity directly. Intended for internal use or testing.
     *
     * @param book the book entity to save
     * @return the saved book entity
     */
    Book saveBook(Book book);

    /**
     * Deletes a book by its ID and returns detailed information about the deleted book.
     *
     * This method retrieves the book from the database, deletes it, and returns a {@link DeleteResponseDTO}
     * containing the book's ID, title, and ISBN. If the book is not found, it throws a {@link BookNotFoundException}.
     *
     * @param id the unique identifier of the book to delete
     * @return a {@link DeleteResponseDTO} containing metadata about the deleted book
     * @throws BookNotFoundException if the book with the given ID does not exist
     */
    DeleteResponseDTO deleteBookWithDetails(String id);

}
