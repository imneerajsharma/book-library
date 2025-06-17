package com.neeraj.book_library.repository;

import com.neeraj.book_library.entity.Book;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for performing CRUD operations on Book documents in MongoDB.
 */
@Repository
public interface BookRepository extends MongoRepository<Book, String> {

    /**
     * Finds a book by its ISBN.
     *
     * @param isbn the unique ISBN identifier
     * @return an Optional containing the book if found, or empty if not
     */
    Optional<Book> findByIsbn(String isbn);

    /**
     * Checks if a book with the given ISBN exists.
     *
     * @param isbn the ISBN to check
     * @return true if a book with the ISBN exists, false otherwise
     */
    boolean existsByIsbn(String isbn);
}
