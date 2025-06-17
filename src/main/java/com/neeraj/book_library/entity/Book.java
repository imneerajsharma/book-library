package com.neeraj.book_library.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;

/**
 * Entity representing a Book document in MongoDB.
 */
@Document(collection = "books")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Book {

    @Id
    private String id;

    private String title;

    private String author;

    @Indexed(unique = true)
    private String isbn;

    private String description;

    @Field("publishedDate")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate publishedDate;

    // Optional: add controlled update methods if removing setters
    public Book withUpdatedInfo(Book other) {
        return Book.builder()
                .id(this.id)
                .title(other.title)
                .author(other.author)
                .isbn(this.isbn) // preserve original
                .description(other.description)
                .publishedDate(other.publishedDate)
                .build();
    }
}

//package com.neeraj.book_library.entity;
//
//import com.fasterxml.jackson.annotation.JsonFormat;
//import lombok.*;
//import org.springframework.data.annotation.Id;
//import org.springframework.data.mongodb.core.mapping.Document;
//import org.springframework.data.mongodb.core.mapping.Field;
//
//import java.time.LocalDate;
//
///**
// * Entity representing a Book document in MongoDB.
// */
//@Document(collection = "books")
//@NoArgsConstructor
//@AllArgsConstructor
//@Getter
//@Setter
//@Builder
//public class Book {
//
//    /**
//     * Unique identifier for the book (MongoDB ObjectId).
//     */
//    @Id
//    private String id;
//
//    /**
//     * Title of the book.
//     */
//    private String title;
//
//    /**
//     * Author of the book.
//     */
//    private String author;
//
//    /**
//     * ISBN number of the book.
//     */
//    private String isbn;
//
//    /**
//     * Optional description or summary of the book.
//     */
//    private String description;
//
//    /**
//     * Date when the book was published.
//     */
//    @Field("publishedDate")
//    @JsonFormat(pattern = "yyyy-MM-dd")
//    private LocalDate publishedDate;
//}
