package com.neeraj.book_library.bootstrap;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.neeraj.book_library.entity.Book;
import com.neeraj.book_library.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.Collections;
import java.util.List;

/**
 * Seeds initial data into the database from a JSON file
 * if the book collection is empty.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final BookRepository bookRepository;
    private final ObjectMapper objectMapper;

    @Value("classpath:data.json")
    private  Resource seedFile;

    @Override
    public void run(String... args) {
        if (isDatabaseEmpty()) {
            log.info("No existing books found. Seeding initial data from {}...", seedFile.getFilename());

            final List<Book> books = loadBooksFromJson(seedFile);

            if (books.isEmpty()) {
                log.warn("Book list is empty or could not be loaded. Skipping seeding.");
                return;
            }

            for (final Book book : books) {
                try {
                    bookRepository.save(book);
                    log.info("Seeded book: {}", book.getTitle());
                } catch (Exception e) {
                    log.warn("Failed to seed book '{}': {}", book.getTitle(), e.getMessage());
                }
            }

            log.info("Finished seeding {} books (with possible partial failures).", books.size());
        } else {
            log.info("Book data already present. Skipping seeding process.");
        }
    }

    private boolean isDatabaseEmpty() {
        return bookRepository.count() == 0;
    }

    private List<Book> loadBooksFromJson(final Resource jsonFile) {
        try (final InputStream inputStream = jsonFile.getInputStream()) {
            return objectMapper.readValue(inputStream, new TypeReference<List<Book>>() {});
        } catch (Exception e) {
            log.error("Failed to read seed data from {}: {}", jsonFile.getFilename(), e.getMessage(), e);
            return Collections.emptyList();
        }
    }
}
