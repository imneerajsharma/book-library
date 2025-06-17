package com.neeraj.book_library.controller;

import com.neeraj.book_library.dto.*;
import com.neeraj.book_library.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<ApiResponseWrapper<BookResponseDTO>> createBook(
            @Valid @RequestBody BookRequestDTO bookRequestDTO) {
        final BookResponseDTO savedBook = bookService.createBook(bookRequestDTO);
        return ResponseEntity.ok(ApiResponseWrapper.success("Book created successfully", savedBook));
    }

    @PostMapping("/bulk")
    public ResponseEntity<ApiResponseWrapper<List<BookResponseDTO>>> createBooksBulk(
            @Valid @RequestBody List<BookRequestDTO> bookRequestDTOs) {
        final List<BookResponseDTO> createdBooks = bookService.createBooksBulk(bookRequestDTOs);
        return ResponseEntity.ok(ApiResponseWrapper.success("Books created successfully", createdBooks));
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponseWrapper<List<BookResponseDTO>>> getAllBooksUnpaged() {
        final List<BookResponseDTO> books = bookService.getAllBooks();
        return ResponseEntity.ok(ApiResponseWrapper.success("Books fetched successfully", books));
    }

    @GetMapping
    public ResponseEntity<ApiResponseWrapper<BookPageResponse>> getBooksPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "asc") String sortDirection,
            @RequestParam(defaultValue = "title") String sortBy) {
        final BookPageResponse paginatedBooks = bookService.getBooksPaginated(page, size, sortDirection, sortBy);
        return ResponseEntity.ok(ApiResponseWrapper.success("Books fetched successfully", paginatedBooks));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseWrapper<BookResponseDTO>> getBookById(@PathVariable String id) {
        final BookResponseDTO book = bookService.getBookById(id);
        return ResponseEntity.ok(ApiResponseWrapper.success("Book fetched successfully", book));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseWrapper<BookResponseDTO>> updateBook(
            @PathVariable String id,
            @Valid @RequestBody BookRequestDTO bookRequestDTO) {
        final BookResponseDTO updatedBook = bookService.updateBook(id, bookRequestDTO);
        return ResponseEntity.ok(ApiResponseWrapper.success("Book updated successfully", updatedBook));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseWrapper<DeleteResponseDTO>> deleteBook(@PathVariable String id) {
        final DeleteResponseDTO deleteResponse = bookService.deleteBook(id);
        return ResponseEntity.ok(ApiResponseWrapper.success("Book deleted successfully", deleteResponse));
    }
}
