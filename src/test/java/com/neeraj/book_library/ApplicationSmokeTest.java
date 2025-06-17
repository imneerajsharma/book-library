package com.neeraj.book_library;

import com.neeraj.book_library.controller.BookController;
import com.neeraj.book_library.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Smoke test to verify that the application context loads
 * and critical beans are properly initialized.
 */
@SpringBootTest
public class ApplicationSmokeTest {

    private static final Logger log = LoggerFactory.getLogger(ApplicationSmokeTest.class);

    @Autowired
    private BookController bookController;

    @Autowired
    private BookRepository bookRepository;

    @Test
    void contextLoadsSuccessfully() {
        assertThat(bookController).isNotNull();
        log.info("BookController bean loaded successfully.");

        assertThat(bookRepository).isNotNull();
        log.info("BookRepository bean loaded successfully.");
    }
}
