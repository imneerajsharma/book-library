package com.neeraj.book_library.service;

import com.neeraj.book_library.dto.BookRequestDTO;
import com.neeraj.book_library.dto.BookResponseDTO;
import com.neeraj.book_library.dto.BookPageResponse;
import com.neeraj.book_library.dto.DeleteResponseDTO;
import com.neeraj.book_library.entity.Book;
import com.neeraj.book_library.exception.BookNotFoundException;
import com.neeraj.book_library.exception.DuplicateBookException;
import com.neeraj.book_library.mapper.BookMapper;
import com.neeraj.book_library.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementation of {@link BookService} that handles all business logic
 * related to creating, retrieving, updating, and deleting book records.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    /**
     * Creates a new book after checking for duplicate ISBN.
     *
     * @param bookRequestDTO the DTO containing book details
     * @return the created book as a response DTO
     * @throws DuplicateBookException if a book with the same ISBN already exists
     */
    @Override
    public BookResponseDTO createBook(BookRequestDTO bookRequestDTO) {
        log.info("Creating book with ISBN: {}", bookRequestDTO.getIsbn());

        bookRepository.findByIsbn(bookRequestDTO.getIsbn()).ifPresent(existing -> {
            log.warn("Duplicate book detected for ISBN: {}", bookRequestDTO.getIsbn());
            throw new DuplicateBookException("Book with ISBN " + bookRequestDTO.getIsbn() + " already exists.");
        });

        Book savedBook = bookRepository.save(bookMapper.toEntity(bookRequestDTO));
        log.info("Book created successfully with ID: {}", savedBook.getId());

        return bookMapper.toResponseDTO(savedBook);
    }

    /**
     * Bulk creates multiple books. No duplicate ISBN check for performance.
     *
     * @param bookRequestDTOs list of book creation requests
     * @return list of created books as response DTOs
     */
    @Override
    public List<BookResponseDTO> createBooksBulk(List<BookRequestDTO> bookRequestDTOs) {
        log.info("Bulk creating {} books", bookRequestDTOs.size());

        final List<Book> booksToSave = bookRequestDTOs.stream()
                .map(bookMapper::toEntity)
                .toList();

        List<Book> savedBooks = bookRepository.saveAll(booksToSave);
        log.info("Successfully saved {} books", savedBooks.size());

        return savedBooks.stream()
                .map(bookMapper::toResponseDTO)
                .toList();
    }

    /**
     * Retrieves a book by its ID.
     *
     * @param id the unique ID of the book
     * @return the book as a response DTO
     * @throws BookNotFoundException if the book does not exist
     */
    @Override
    public BookResponseDTO getBookById(String id) {
        log.debug("Fetching book with ID: {}", id);

        final Book book = bookRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Book not found with ID: {}", id);
                    return new BookNotFoundException("Book not found with id: " + id);
                });

        return bookMapper.toResponseDTO(book);
    }

    /**
     * Retrieves all books without pagination.
     *
     * @return list of all books as response DTOs
     */
    @Override
    public List<BookResponseDTO> getAllBooks() {
        log.debug("Fetching all books (unpaged)");

        return bookRepository.findAll()
                .stream()
                .map(bookMapper::toResponseDTO)
                .toList();
    }

    /**
     * Retrieves books in a paginated format with support for dynamic sorting.
     *
     * @param page          the 0-based page index to retrieve
     * @param size          the number of books per page
     * @param sortDirection the direction of sorting: "asc" for ascending or "desc" for descending (case-insensitive)
     * @param sortBy        the field name to sort by (e.g., "title", "author", "publishedDate")
     * @return a paginated response containing books and pagination metadata
     */
    @Override
    public BookPageResponse getBooksPaginated(int page, int size, String sortDirection, String sortBy) {
        log.debug("Fetching books paginated - page: {}, size: {}, sortBy: {}, sortDirection: {}",
                page, size, sortBy, sortDirection);

        Sort.Direction direction = "desc".equalsIgnoreCase(sortDirection) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        Page<Book> bookPage = bookRepository.findAll(pageable);

        return BookPageResponse.builder()
                .books(bookPage.getContent().stream().map(bookMapper::toResponseDTO).toList())
                .totalElements(bookPage.getTotalElements())
                .totalPages(bookPage.getTotalPages())
                .page(bookPage.getNumber())
                .size(bookPage.getSize())
                .build();
    }


    /**
     * Updates an existing book by ID.
     *
     * @param id              the ID of the book to update
     * @param bookRequestDTO  the DTO containing updated book details
     * @return the updated book as a response DTO
     * @throws BookNotFoundException if the book does not exist
     */
    @Override
    public BookResponseDTO updateBook(String id, BookRequestDTO bookRequestDTO) {
        log.info("Updating book with ID: {}", id);

        final Book existingBook = bookRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Cannot update. Book not found with ID: {}", id);
                    return new BookNotFoundException("Book not found with id: " + id);
                });

        final Book updatedBook = bookMapper.updateEntityFromRequest(bookRequestDTO, existingBook);
        Book savedBook = bookRepository.save(updatedBook);

        log.info("Book updated successfully with ID: {}", savedBook.getId());
        return bookMapper.toResponseDTO(savedBook);
    }

    /**
     * Deletes a book by ID.
     *
     * @param id the ID of the book to delete
     * @return a response message indicating successful deletion
     * @throws BookNotFoundException if the book does not exist
     */
    @Override
    public DeleteResponseDTO deleteBook(String id) {
        log.info("Deleting book with ID: {}", id);

        if (!bookRepository.existsById(id)) {
            log.error("Delete failed. Book not found with ID: {}", id);
            throw new BookNotFoundException("Book not found with id: " + id);
        }

        bookRepository.deleteById(id);
        log.info("Book deleted successfully with ID: {}", id);
        return DeleteResponseDTO.builder()
                .message("Book deleted successfully")
                .build();
    }

    /**
     * Saves a Book entity directly to the database.
     * Intended for internal use such as testing or seeding.
     *
     * @param book the book entity to save
     * @return the saved book entity
     */
    @Override
    public Book saveBook(Book book) {
        log.debug("Saving book directly via saveBook(Book). ISBN: {}", book.getIsbn());
        return bookRepository.save(book);
    }
}
