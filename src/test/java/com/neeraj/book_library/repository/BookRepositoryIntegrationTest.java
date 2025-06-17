package com.neeraj.book_library.repository;

import com.neeraj.book_library.entity.Book;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
class BookRepositoryIntegrationTest {

    @Autowired
    private BookRepository bookRepository;

    @AfterEach
    void cleanUp() {
        bookRepository.deleteAll();
    }

    @Test
    @DisplayName("Saving and finding book by ID should return the same book")
    void saveAndFindById_shouldReturnBook() {
        Book book = Book.builder()
                .title("Sample Book")
                .author("Author")
                .isbn("123456")
                .publishedDate(LocalDate.of(2024, 1, 1))
                .build();

        Book saved = bookRepository.save(book);
        Optional<Book> found = bookRepository.findById(saved.getId());

        assertTrue(found.isPresent());
        assertEquals(book.getTitle(), found.get().getTitle());
        assertEquals(book.getIsbn(), found.get().getIsbn());
    }

    @Test
    @DisplayName("Deleting book by ID should remove it from DB")
    void deleteById_shouldRemoveBook() {
        Book book = Book.builder()
                .title("To Be Deleted")
                .author("Author X")
                .isbn("ISBN-DELETE")
                .publishedDate(LocalDate.now())
                .build();

        Book saved = bookRepository.save(book);
        bookRepository.deleteById(saved.getId());

        assertFalse(bookRepository.findById(saved.getId()).isPresent());
    }

    @Test
    @DisplayName("Finding a book by ISBN should return correct result")
    void findByIsbn_shouldReturnBook() {
        Book book = Book.builder()
                .title("Searchable Book")
                .author("Author")
                .isbn("FIND-ME")
                .publishedDate(LocalDate.now())
                .build();

        bookRepository.save(book);
        Optional<Book> found = bookRepository.findByIsbn("FIND-ME");

        assertTrue(found.isPresent());
        assertEquals(book.getTitle(), found.get().getTitle());
    }

    @Test
    @DisplayName("Finding non-existent ISBN should return empty optional")
    void findByNonExistentIsbn_shouldReturnEmpty() {
        Optional<Book> found = bookRepository.findByIsbn("NO-SUCH-ISBN");
        assertFalse(found.isPresent());
    }

    @Test
    @DisplayName("Saving a book with duplicate ISBN should throw exception")
    void savingBookWithDuplicateIsbn_shouldThrowException() {
        Book book1 = Book.builder()
                .title("Book 1")
                .author("Author 1")
                .isbn("DUPLICATE-ISBN")
                .publishedDate(LocalDate.now())
                .build();

        Book book2 = Book.builder()
                .title("Book 2")
                .author("Author 2")
                .isbn("DUPLICATE-ISBN")
                .publishedDate(LocalDate.now())
                .build();

        bookRepository.save(book1);
        Exception ex = assertThrows(Exception.class, () -> bookRepository.save(book2));
        assertTrue(ex.getMessage().contains("duplicate") || ex.getMessage().contains("E11000"));
    }
    @Test
    @DisplayName("Should return count of books in the repository")
    void count_shouldReturnCorrectSize() {
        bookRepository.save(Book.builder().title("A").author("A1").isbn("A-ISBN").publishedDate(LocalDate.now()).build());
        bookRepository.save(Book.builder().title("B").author("B1").isbn("B-ISBN").publishedDate(LocalDate.now()).build());

        long count = bookRepository.count();

        assertEquals(2, count);
    }

    @Test
    @DisplayName("Should return true if book with ISBN exists")
    void existsByIsbn_shouldReturnTrueIfExists() {
        String isbn = "EXIST-ISBN";
        bookRepository.save(Book.builder().title("Exist Book").author("X").isbn(isbn).publishedDate(LocalDate.now()).build());

        boolean exists = bookRepository.existsByIsbn(isbn);

        assertTrue(exists);
    }

    @Test
    @DisplayName("Should return false for non-existent ISBN")
    void existsByIsbn_shouldReturnFalseIfNotExists() {
        boolean exists = bookRepository.existsByIsbn("NOPE-ISBN");

        assertFalse(exists);
    }

}
