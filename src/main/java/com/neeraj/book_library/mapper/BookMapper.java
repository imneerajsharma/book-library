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
        if (dto == null) {
            return null;
        }

        return Book.builder()
                .title(dto.getTitle())
                .author(dto.getAuthor())
                .isbn(dto.getIsbn())
                .description(dto.getDescription())
                .publishedDate(dto.getPublishedDate())
                .build();
    }

    /**
     * Converts a Book entity to a BookResponseDTO.
     *
     * @param entity the Book entity
     * @return BookResponseDTO or null if input is null
     */
    public BookResponseDTO toResponseDTO(Book entity) {
        if (entity == null) {
            return null;
        }

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
        if (dto == null) {
            return existing; // no changes if update payload is null
        }

        return Book.builder()
                .id(existing.getId())           // preserve ID
                .isbn(existing.getIsbn())       // preserve ISBN
                .title(dto.getTitle())
                .author(dto.getAuthor())
                .description(dto.getDescription())
                .publishedDate(dto.getPublishedDate())
                .build();
    }
}
