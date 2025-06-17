package com.neeraj.book_library.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

/**
 * DTO representing a paginated response for books.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Paginated response for books")
public class BookPageResponse {

    /**
     * List of BookResponseDTOs on the current page.
     */
    @Schema(description = "List of books on the current page")
    private List<BookResponseDTO> books;

    /**
     * Total number of book elements in the database.
     */
    @Schema(description = "Total number of books")
    private long totalElements;

    /**
     * Total number of pages calculated based on page size.
     */
    @Schema(description = "Total number of pages available")
    private int totalPages;

    /**
     * Current page number (0-based index).
     */
    @Schema(description = "Current page number")
    private int page;

    /**
     * Page size (number of items per page).
     */
    @Schema(description = "Size of the page")
    private int size;
}
