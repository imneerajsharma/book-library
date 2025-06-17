package com.neeraj.book_library.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;

/**
 * DTO used for sending book data in API responses.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Schema(description = "Book response data returned from the API")
public class BookResponseDTO {

    @Schema(description = "Unique identifier of the book", example = "665f17acb6e9e132b6bb0e91")
    private String id;

    @Schema(description = "Title of the book", example = "Effective Java")
    private String title;

    @Schema(description = "Author of the book", example = "Joshua Bloch")
    private String author;

    @Schema(description = "Brief description about the book", example = "A comprehensive guide to best practices in Java programming")
    private String description;

    @Schema(description = "ISBN number of the book", example = "978-0134685991")
    private String isbn;

    @Schema(description = "Published date of the book", type = "string", format = "date", example = "2024-06-15")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate publishedDate;
}
