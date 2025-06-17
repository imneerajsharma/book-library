package com.neeraj.book_library.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

/**
 * DTO used for creating or updating book data via API requests.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Schema(description = "Book creation/update request payload")
public class BookRequestDTO {

    @Schema(description = "Title of the book", example = "Effective Java")
    @NotBlank(message = "Title must not be blank")
    @Size(max = 100, message = "Title must not exceed 100 characters")
    private String title;

    @Schema(description = "Author of the book", example = "Joshua Bloch")
    @NotBlank(message = "Author must not be blank")
    @Size(max = 100, message = "Author must not exceed 100 characters")
    private String author;

    @Schema(description = "Short description of the book", example = "A comprehensive guide to best practices in Java programming")
    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    private String description;

    @Schema(description = "ISBN identifier", example = "978-0134685991")
    @NotBlank(message = "ISBN must not be blank")
    @Size(max = 20, message = "ISBN must not exceed 20 characters")
    private String isbn;

    @Schema(
            type = "string",
            format = "date",
            example = "2024-06-15",
            description = "The published date of the book (must not be null or in the future)"
    )
    @JsonFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "Published date must not be null")
    @PastOrPresent(message = "Published date cannot be in the future")
    private LocalDate publishedDate;
}
