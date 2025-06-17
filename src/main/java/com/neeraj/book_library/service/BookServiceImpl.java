package com.neeraj.book_library.service;

import com.google.common.annotations.VisibleForTesting;
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
 * Implementation of BookService handling core business logic.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    /**
     * Creates a new book after ensuring the ISBN is unique.
     *
     * @param bookRequestDTO the request DTO with book data
     * @return the created book in response DTO format
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
     * Bulk creates books without duplicate validation for performance.
     *
     * @param bookRequestDTOs list of books to be created
     * @return list of saved books as response DTOs
     */
    @Override
    public List<BookResponseDTO> createBooksBulk(List<BookRequestDTO> bookRequestDTOs) {
        log.info("Bulk creating {} books", bookRequestDTOs.size());

        List<Book> booksToSave = bookRequestDTOs.stream()
                .map(bookMapper::toEntity)
                .toList();

        List<Book> savedBooks = bookRepository.saveAll(booksToSave);

        log.info("Successfully saved {} books", savedBooks.size());

        return savedBooks.stream()
                .map(bookMapper::toResponseDTO)
                .toList();
    }

    /**
     * Retrieves a book by ID.
     *
     * @param id MongoDB ID of the book
     * @return corresponding book as a response DTO
     */
    @Override
    public BookResponseDTO getBookById(String id) {
        log.debug("Fetching book with ID: {}", id);

        Book book = bookRepository.findById(id)
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
    public List<BookResponseDTO> getAllBooksUnpaged() {
        log.debug("Fetching all books (unpaged)");

        return bookRepository.findAll()
                .stream()
                .map(bookMapper::toResponseDTO)
                .toList();
    }

    /**
     * Retrieves books with pagination support.
     *
     * @param page current page (0-indexed)
     * @param size number of items per page
     * @return a paginated response with book data
     */
    @Override
    public BookPageResponse getAllBooks(int page, int size) {
        log.debug("Fetching books paginated - page: {}, size: {}", page, size);

        Pageable pageable = PageRequest.of(page, size, Sort.by("title").ascending());
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
     * Updates an existing book based on its ID.
     *
     * @param id              the book ID
     * @param bookRequestDTO  updated book fields
     * @return the updated book as a response DTO
     */
    @Override
    public BookResponseDTO updateBook(String id, BookRequestDTO bookRequestDTO) {
        log.info("Updating book with ID: {}", id);

        Book existingBook = bookRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Cannot update. Book not found with ID: {}", id);
                    return new BookNotFoundException("Book not found with id: " + id);
                });

        bookMapper.updateEntityFromRequest(bookRequestDTO, existingBook);
        Book updatedBook = bookRepository.save(existingBook);

        log.info("Book updated successfully with ID: {}", updatedBook.getId());
        return bookMapper.toResponseDTO(updatedBook);
    }

    /**
     * Deletes a book by ID and returns deletion details.
     *
     * @param id the ID of the book to delete
     * @return a structured delete response
     */
    @Override
    public DeleteResponseDTO deleteBook(String id) {
        log.info("Deleting book with ID: {}", id);

        Book book = bookRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Delete failed. Book not found with ID: {}", id);
                    return new BookNotFoundException("Book not found with id: " + id);
                });

        bookRepository.deleteById(id);

        log.info("Book deleted successfully with ID: {}", id);
        return new DeleteResponseDTO(
                "Book deleted successfully",
                book.getId(),
                book.getIsbn(),
                book.getTitle()
        );
    }

    /**
     * Persists a Book entity. Intended for internal use only (e.g., test seeding).
     *
     * @param book the book to persist
     * @return the persisted book
     */

    @Override
    public Book saveBook(Book book) {
        log.debug("Saving book directly via saveBook(Book). ISBN: {}", book.getIsbn());
        return bookRepository.save(book);
    }
    @Override
    public DeleteResponseDTO deleteBookWithDetails(String id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Book not found with ID: " + id));

        bookRepository.deleteById(id);

        return DeleteResponseDTO.builder()
                .message("Book deleted successfully")
                .deletedBookId(book.getId())
                .isbn(book.getIsbn())
                .title(book.getTitle())
                .build();
    }

}