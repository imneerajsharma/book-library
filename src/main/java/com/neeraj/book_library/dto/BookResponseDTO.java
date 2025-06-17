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
@Getter
@Setter
@Builder
public class BookResponseDTO {

    private String id;

    private String title;

    private String author;

    private String description;

    private String isbn;

    @Schema(type = "string", format = "date", example = "2024-06-15")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate publishedDate;
}
