package com.neeraj.book_library.controller;

import com.neeraj.book_library.dto.*;
import com.neeraj.book_library.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    @Operation(summary = "Create a new book")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Book created successfully"),
            @ApiResponse(responseCode = "400", description = "Validation error or duplicate ISBN")
    })
    @PostMapping
    public ResponseEntity<ApiResponseWrapper<BookResponseDTO>> createBook(@Valid @RequestBody BookRequestDTO bookRequestDTO) {
        BookResponseDTO savedBook = bookService.createBook(bookRequestDTO);
        log.info("Created book with ID: {}", savedBook.getId());
        return ResponseEntity.ok(ApiResponseWrapper.success("Book created successfully", savedBook));
    }

    @PostMapping("/bulk")
    public ResponseEntity<ApiResponseWrapper<List<BookResponseDTO>>> createBooksBulk(@Valid @RequestBody List<BookRequestDTO> bookRequestDTOs) {
        List<BookResponseDTO> savedBooks = bookService.createBooksBulk(bookRequestDTOs);
        log.info("Bulk created {} books", savedBooks.size());
        return ResponseEntity.ok(ApiResponseWrapper.success("Books created successfully", savedBooks));
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponseWrapper<List<BookResponseDTO>>> getAllBooksUnpaged() {
        List<BookResponseDTO> books = bookService.getAllBooksUnpaged();
        return ResponseEntity.ok(ApiResponseWrapper.success("Books fetched successfully", books));
    }

    @GetMapping
    public ResponseEntity<ApiResponseWrapper<BookPageResponse>> getAllBooks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        BookPageResponse books = bookService.getAllBooks(page, size);
        return ResponseEntity.ok(ApiResponseWrapper.success("Books fetched successfully", books));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseWrapper<BookResponseDTO>> getBookById(@PathVariable String id) {
        BookResponseDTO bookDTO = bookService.getBookById(id);
        return ResponseEntity.ok(ApiResponseWrapper.success("Book fetched successfully", bookDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseWrapper<BookResponseDTO>> updateBook(@PathVariable String id, @Valid @RequestBody BookRequestDTO bookRequestDTO) {
        BookResponseDTO updatedBook = bookService.updateBook(id, bookRequestDTO);
        log.info("Updated book with ID: {}", id);
        return ResponseEntity.ok(ApiResponseWrapper.success("Book updated successfully", updatedBook));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseWrapper<DeleteResponseDTO>> deleteBook(@PathVariable String id) {
        DeleteResponseDTO deleted = bookService.deleteBookWithDetails(id);
        log.info("Deleted book with ID: {}", id);
        return ResponseEntity.ok(ApiResponseWrapper.success("Book deleted successfully", deleted));
    }
}



//package com.neeraj.book_library.controller;
//
//import com.neeraj.book_library.dto.BookDTO;
//import com.neeraj.book_library.dto.BookPageResponse;
//import com.neeraj.book_library.service.BookService;
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.responses.ApiResponse;
//import io.swagger.v3.oas.annotations.responses.ApiResponses;
//import jakarta.validation.Valid;
//import org.modelmapper.ModelMapper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
///**
// * REST controller for managing book-related operations.
// * Handles CRUD operations and pagination via endpoints.
// */
//@RestController
//@RequestMapping("/api/books")
//public class BookController {
//
//    private final BookService bookService;
//    private final ModelMapper modelMapper;
//
//    @Autowired
//    public BookController(BookService bookService, ModelMapper modelMapper) {
//        this.bookService = bookService;
//        this.modelMapper = modelMapper;
//    }
//
//    /**
//     * Creates a new book.
//     *
//     * @param bookDTO the book to create
//     * @return the saved book
//     */
//    @Operation(summary = "Create a new book")
//    @ApiResponses({
//            @ApiResponse(responseCode = "200", description = "Book created successfully"),
//            @ApiResponse(responseCode = "400", description = "Validation error or duplicate ISBN")
//    })
//    @PostMapping
//    public ResponseEntity<BookDTO> createBook(@Valid @RequestBody BookDTO bookDTO) {
//        BookDTO savedBook = bookService.createBook(bookDTO);
//        // add logger
//        return ResponseEntity.ok(savedBook);
//    }
//    /**
//     * Bulk creates books.
//     *
//     * @param bookDTOs list of books to create
//     * @return list of saved books
//     */
//    @Operation(summary = "Bulk create books")
//    @ApiResponses({
//            @ApiResponse(responseCode = "200", description = "Books created successfully"),
//            @ApiResponse(responseCode = "400", description = "Validation error")
//    })
//    @PostMapping("/bulk")
//    public ResponseEntity<List<BookDTO>> createBooksBulk(@Valid @RequestBody List<BookDTO> bookDTOs) {
//        List<BookDTO> savedBooks = bookService.createBooksBulk(bookDTOs);
//        return ResponseEntity.ok(savedBooks);
//    }
//
//    /**
//     * Retrieves all books without pagination.
//     *
//     * @return list of all books
//     */
//    @Operation(summary = "Get all books without pagination")
//    @ApiResponse(responseCode = "200", description = "Books fetched successfully")
//    @GetMapping("/all")
//    public ResponseEntity<List<BookDTO>> getAllBooksUnpaged() {
//        return ResponseEntity.ok(bookService.getAllBooksUnpaged());
//    }
//
//    /**
//     * Retrieves books in a paginated format.
//     *
//     * @param page page number (0-indexed)
//     * @param size number of items per page
//     * @return paginated book response
//     */
//    @Operation(summary = "Get paginated list of books")
//    @ApiResponses({
//            @ApiResponse(responseCode = "200", description = "Books fetched successfully")
//    })
//    @GetMapping
//    public ResponseEntity<BookPageResponse> getAllBooks(
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "10") int size
//    ) {
//        BookPageResponse books = bookService.getAllBooks(page, size);
//        return ResponseEntity.ok(books);
//    }
//
//    /**
//     * Retrieves a book by its ID.
//     *
//     * @param id the book ID
//     * @return the book if found
//     */
//    @Operation(summary = "Get book by ID")
//    @ApiResponses({
//            @ApiResponse(responseCode = "200", description = "Book found"),
//            @ApiResponse(responseCode = "404", description = "Book not found")
//    })
//    @GetMapping("/{id}")
//    public ResponseEntity<BookDTO> getBookById(@PathVariable String id) {
//        BookDTO bookDTO = bookService.getBookById(id);
//        return ResponseEntity.ok(bookDTO);
//    }
//
//    /**
//     * Updates a book by its ID.
//     *
//     * @param id      the book ID
//     * @param bookDTO the updated book data
//     * @return the updated book
//     */
//    @Operation(summary = "Update book by ID")
//    @ApiResponses({
//            @ApiResponse(responseCode = "200", description = "Book updated successfully"),
//            @ApiResponse(responseCode = "404", description = "Book not found"),
//            @ApiResponse(responseCode = "400", description = "Validation error")
//    })
//
////    @PutMapping("/{id}")
////    public ResponseEntity<Map<String, Object>> updateBook(@PathVariable String id, @Valid @RequestBody BookDTO bookDTO) {
////        BookDTO updatedBook = bookService.updateBook(id, bookDTO);
////
////        Map<String, Object> response = new HashMap<>();
////        response.put("message", "Book updated successfully");
////        response.put("data", updatedBook);
////
////        return ResponseEntity.ok(response);
////    }
//    @PutMapping("/{id}")
//    public ResponseEntity<BookDTO> updateBook(@PathVariable String id, @Valid @RequestBody BookDTO bookDTO) {
//        BookDTO updatedBook = bookService.updateBook(id, bookDTO);
//        return ResponseEntity.ok(updatedBook);
//    }
//
//    /**
//     * Deletes a book by its ID.
//     *
//     * @param id the book ID
//     * @return success message including book title and ISBN
//     */
//    @Operation(summary = "Delete book by ID")
//    @ApiResponses({
//            @ApiResponse(responseCode = "200", description = "Book deleted successfully"),
//            @ApiResponse(responseCode = "404", description = "Book not found")
//    })
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<String> deleteBook(@PathVariable String id) {
//        String message = bookService.deleteBook(id);
//        return ResponseEntity.ok(message);
//    }
//
//}