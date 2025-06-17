package com.neeraj.book_library.mapper;

import com.neeraj.book_library.dto.BookRequestDTO;
import com.neeraj.book_library.dto.BookResponseDTO;
import com.neeraj.book_library.entity.Book;
import org.springframework.stereotype.Component;

/**
 * Mapper utility to convert between Book entity and its DTOs.
 * Implements manual, explicit mapping for immutability and clarity.
 */
@Component
public class BookMapper {

    /**
     * Converts a BookRequestDTO to a Book entity.
     *
     * @param dto the incoming DTO
     * @return Book entity or null if input is null
     */
    public Book toEntity(BookRequestDTO dto) {
        if (dto == null) return null;

        return populateCommonFields(dto)
                .isbn(dto.getIsbn())
                .build();
    }

    /**
     * Converts a Book entity to a BookResponseDTO.
     *
     * @param entity the Book entity
     * @return BookResponseDTO or null if input is null
     */
    public BookResponseDTO toResponseDTO(Book entity) {
        if (entity == null) return null;

        return BookResponseDTO.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .author(entity.getAuthor())
                .isbn(entity.getIsbn())
                .description(entity.getDescription())
                .publishedDate(entity.getPublishedDate())
                .build();
    }

    /**
     * Updates an existing Book entity from the incoming DTO.
     * Preserves immutable fields like ID and ISBN.
     *
     * @param dto      the incoming update request
     * @param existing the existing Book entity
     * @return updated Book entity (never null)
     */
    public Book updateEntityFromRequest(BookRequestDTO dto, Book existing) {
        if (dto == null) return existing;

        return populateCommonFields(dto)
                .id(existing.getId())
                .isbn(existing.getIsbn())
                .build();
    }

    /**
     * Helper method to map common fields from DTO to Book builder.
     *
     * @param dto BookRequestDTO
     * @return Book.BookBuilder with shared fields populated
     */
    private Book.BookBuilder populateCommonFields(BookRequestDTO dto) {
        return Book.builder()
                .title(dto.getTitle())
                .author(dto.getAuthor())
                .description(dto.getDescription())
                .publishedDate(dto.getPublishedDate());
    }
}
