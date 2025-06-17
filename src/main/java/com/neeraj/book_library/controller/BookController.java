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

