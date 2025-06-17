package com.neeraj.book_library;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point of the Book Library Spring Boot application.
 * It initializes and runs the application context.
 */
@SpringBootApplication
public class BookLibraryApplication {


	/**
	 * Main method to launch the Spring Boot application.
	 *
	 * @param args command-line arguments passed to the application
	 */
	public static void main(String[] args) {
		SpringApplication.run(BookLibraryApplication.class, args);
	}

}
