package com.neeraj.book_library.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

/**
 * DTO returned after a book is successfully deleted.
 */
@Schema(description = "Response returned after successful book deletion")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeleteResponseDTO {

    @Schema(description = "Confirmation message about deletion", example = "Book deleted successfully")
    private String message;

    @Schema(description = "ID of the deleted book", example = "64adcefba32a2a58d88df123")
    private String deletedBookId;

    @Schema(description = "ISBN of the deleted book", example = "9781234567890")
    private String isbn;

    @Schema(description = "Title of the deleted book", example = "Clean Code")
    private String title;
}
