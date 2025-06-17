package com.neeraj.book_library.bootstrap;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.neeraj.book_library.entity.Book;
import com.neeraj.book_library.repository.BookRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.TestPropertySource;

import java.io.InputStream;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class DataSeederTest {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void cleanUp() {
        bookRepository.deleteAll();
    }

    @Test
    @DisplayName("Should seed books from data.json when DB is empty")
    void shouldSeedBooksWhenDatabaseIsEmpty() throws Exception {
        long initialCount = bookRepository.count();
        assertThat(initialCount).isEqualTo(0);

        InputStream inputStream = new ClassPathResource("data.json").getInputStream();
        List<Book> books = objectMapper.readValue(inputStream,
                objectMapper.getTypeFactory().constructCollectionType(List.class, Book.class));

        bookRepository.saveAll(books);

        List<Book> savedBooks = bookRepository.findAll();
        assertThat(savedBooks).hasSize(books.size());
        assertThat(savedBooks).extracting(Book::getIsbn).containsAll(
                books.stream().map(Book::getIsbn).toList()
        );
    }

    @Test
    @DisplayName("Should not seed books if database is not empty")
    void shouldNotSeedIfBooksAlreadyExist() throws Exception {
        Book existing = Book.builder()
                .title("Pre-seeded Book")
                .author("Test Author")
                .isbn("PRESEED-123")
                .publishedDate(java.time.LocalDate.now())
                .build();

        bookRepository.save(existing);

        long count = bookRepository.count();
        assertThat(count).isGreaterThan(0);
    }
}
