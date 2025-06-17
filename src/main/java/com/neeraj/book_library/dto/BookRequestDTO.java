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
@Getter
@Setter
@Builder
public class BookRequestDTO {

    @NotBlank(message = "Title must not be blank")
    @Size(max = 100, message = "Title must not exceed 100 characters")
    private String title;

    @NotBlank(message = "Author must not be blank")
    @Size(max = 100, message = "Author must not exceed 100 characters")
    private String author;

    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    private String description;

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
