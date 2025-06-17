package com.neeraj.book_library.bootstrap;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.neeraj.book_library.entity.Book;
import com.neeraj.book_library.repository.BookRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.List;

@Slf4j
@Component
public class DataSeeder implements CommandLineRunner {

    private final BookRepository bookRepository;
    private final ObjectMapper objectMapper;
    private final Resource seedFile;

    public DataSeeder(BookRepository bookRepository,
                      ObjectMapper objectMapper,
                      @Value("classpath:data.json") Resource seedFile) {
        this.bookRepository = bookRepository;
        this.objectMapper = objectMapper;
        this.seedFile = seedFile;
    }

    @Override
    public void run(String... args) {
        if (isDatabaseEmpty()) {
            log.info("No existing books found. Seeding initial data from {}...", seedFile.getFilename());
            List<Book> books = loadBooksFromJson(seedFile);

            if (books == null || books.isEmpty()) {
                log.warn("Book list is empty or could not be loaded. Skipping seeding.");
                return;
            }

            bookRepository.saveAll(books);
            log.info("Successfully seeded {} books into MongoDB.", books.size());
        } else {
            log.info("Book data already present. Skipping seeding process.");
        }
    }

    private boolean isDatabaseEmpty() {
        return bookRepository.count() == 0;
        // Or better (if you add a method in repository): return !bookRepository.existsAny();
    }

    private List<Book> loadBooksFromJson(Resource jsonFile) {
        try (InputStream inputStream = jsonFile.getInputStream()) {
            return objectMapper.readValue(inputStream, new TypeReference<List<Book>>() {});
        } catch (Exception e) {
            log.error("Failed to read seed data from {}: {}", jsonFile.getFilename(), e.getMessage(), e);
            return null;
        }
    }
}
