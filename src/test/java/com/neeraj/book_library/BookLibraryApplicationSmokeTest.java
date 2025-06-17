package com.neeraj.book_library;

import com.neeraj.book_library.controller.BookController;
import com.neeraj.book_library.repository.BookRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * ✅ Combined application context smoke test.
 * Verifies the Spring Boot application context loads correctly,
 * and that essential beans are initialized.
 */
@SpringBootTest
class BookLibraryApplicationSmokeTest {

    private static final Logger log = LoggerFactory.getLogger(BookLibraryApplicationSmokeTest.class);

    @Autowired
    private BookController bookController;

    @Autowired
    private BookRepository bookRepository;

    @Test
    @DisplayName("🟢 Spring context loads without exceptions")
    void contextLoadsWithoutErrors() {
        log.info("[SmokeTest] Application context loaded successfully.");
        // No assertions needed—test passes if context loads without exception
    }

    @Test
    @DisplayName("✅ BookController bean is loaded")
    void bookControllerBeanIsLoaded() {
        assertThat(bookController).isNotNull();
        log.info("[SmokeTest] BookController bean loaded successfully.");
    }

    @Test
    @DisplayName("✅ BookRepository bean is loaded")
    void bookRepositoryBeanIsLoaded() {
        assertThat(bookRepository).isNotNull();
        log.info("[SmokeTest] BookRepository bean loaded successfully.");
    }
}
