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
    List<BookResponseDTO> getAllBooks();

    /**
     * Retrieves books in a paginated format with support for dynamic sorting.
     *
     * @param page          the 0-based page index to retrieve
     * @param size          the number of books per page
     * @param sortDirection the direction of sorting: "asc" for ascending or "desc" for descending
     * @param sortBy        the field to sort by (e.g., "title", "author", "publishedDate")
     * @return a paginated response containing books and metadata
     */
    BookPageResponse getBooksPaginated(int page, int size, String sortDirection, String sortBy);


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
     * Deletes a book by its ID and returns detailed information about the deleted book.
     *
     * @param id the ID of the book to delete
     * @return a {@link DeleteResponseDTO} with metadata about the deleted book
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
}
