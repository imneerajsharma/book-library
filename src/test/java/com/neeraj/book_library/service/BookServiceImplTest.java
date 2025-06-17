package com.neeraj.book_library.service;

import com.neeraj.book_library.dto.BookPageResponse;
import com.neeraj.book_library.dto.BookRequestDTO;
import com.neeraj.book_library.dto.BookResponseDTO;
import com.neeraj.book_library.dto.DeleteResponseDTO;
import com.neeraj.book_library.entity.Book;
import com.neeraj.book_library.exception.BookNotFoundException;
import com.neeraj.book_library.exception.DuplicateBookException;
import com.neeraj.book_library.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class BookServiceImplTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private BookServiceImpl bookService;

    private Book book;
    private BookRequestDTO requestDTO;
    private BookResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        book = Book.builder()
                .id("1")
                .title("Effective Java")
                .author("Joshua Bloch")
                .isbn("978-0134685991")
                .description("Java best practices")
                .publishedDate(LocalDate.of(2008, 8, 11))
                .build();

        requestDTO = BookRequestDTO.builder()
                .title("Effective Java")
                .author("Joshua Bloch")
                .isbn("978-0134685991")
                .description("Java best practices")
                .publishedDate(LocalDate.of(2008, 8, 11))
                .build();

        responseDTO = BookResponseDTO.builder()
                .id("1")
                .title("Effective Java")
                .author("Joshua Bloch")
                .isbn("978-0134685991")
                .description("Java best practices")
                .publishedDate(LocalDate.of(2008, 8, 11))
                .build();
    }

    @Test
    void shouldCreateBookSuccessfully() {
        when(bookRepository.findByIsbn(requestDTO.getIsbn())).thenReturn(Optional.empty());
        when(modelMapper.map(requestDTO, Book.class)).thenReturn(book);
        when(bookRepository.save(book)).thenReturn(book);
        when(modelMapper.map(book, BookResponseDTO.class)).thenReturn(responseDTO);

        BookResponseDTO result = bookService.createBook(requestDTO);

        assertNotNull(result);
        assertEquals("Effective Java", result.getTitle());
    }

    @Test
    void shouldCreateMultipleBooksSuccessfully() {
        List<BookRequestDTO> requestList = List.of(requestDTO);
        List<Book> bookList = List.of(book);
        List<BookResponseDTO> responseList = List.of(responseDTO);

        when(modelMapper.map(requestDTO, Book.class)).thenReturn(book);
        when(bookRepository.saveAll(bookList)).thenReturn(bookList);
        when(modelMapper.map(book, BookResponseDTO.class)).thenReturn(responseDTO);

        List<BookResponseDTO> result = bookService.createBooksBulk(requestList);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Effective Java", result.get(0).getTitle());

        verify(bookRepository, times(1)).saveAll(bookList);
    }

    @Test
    void shouldReturnBookByIdWhenFound() {
        when(bookRepository.findById("1")).thenReturn(Optional.of(book));
        when(modelMapper.map(book, BookResponseDTO.class)).thenReturn(responseDTO);

        BookResponseDTO result = bookService.getBookById("1");

        assertEquals("Effective Java", result.getTitle());
    }

    @Test
    void shouldThrowExceptionWhenBookByIdNotFound() {
        when(bookRepository.findById("1")).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class, () -> bookService.getBookById("1"));
    }

    @Test
    void shouldReturnPaginatedListOfBooks() {
        Page<Book> bookPage = new PageImpl<>(List.of(book));
        when(bookRepository.findAll(any(Pageable.class))).thenReturn(bookPage);
        when(modelMapper.map(book, BookResponseDTO.class)).thenReturn(responseDTO);

        BookPageResponse response = bookService.getAllBooks(0, 10);

        assertEquals(1, response.getTotalElements());
        assertEquals("Effective Java", response.getBooks().get(0).getTitle());
    }

    @Test
    void shouldUpdateBookWhenFound() {
        when(bookRepository.findById("1")).thenReturn(Optional.of(book));
        when(modelMapper.map(requestDTO, Book.class)).thenReturn(book);
        when(bookRepository.save(book)).thenReturn(book);
        when(modelMapper.map(book, BookResponseDTO.class)).thenReturn(responseDTO);

        BookResponseDTO updated = bookService.updateBook("1", requestDTO);

        assertEquals("Effective Java", updated.getTitle());
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistentBook() {
        when(bookRepository.findById("1")).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class, () -> bookService.updateBook("1", requestDTO));
    }

    @Test
    void shouldDeleteBookWhenFound() {
        when(bookRepository.existsById("1")).thenReturn(true);
        doNothing().when(bookRepository).deleteById("1");

        assertDoesNotThrow(() -> bookService.deleteBook("1"));
        verify(bookRepository).deleteById("1");
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistentBook() {
        when(bookRepository.findById("1")).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class, () -> bookService.deleteBook("1"));
    }

    @Test
    void shouldReturnAllBooksUnpaged() {
        when(bookRepository.findAll()).thenReturn(List.of(book));
        when(modelMapper.map(book, BookResponseDTO.class)).thenReturn(responseDTO);

        List<BookResponseDTO> books = bookService.getAllBooksUnpaged();

        assertEquals(1, books.size());
        assertEquals("Effective Java", books.get(0).getTitle());
    }

    @Test
    void shouldSaveBookSuccessfully() {
        when(bookRepository.save(book)).thenReturn(book);
        Book result = bookService.saveBook(book);
        assertEquals("Effective Java", result.getTitle());
    }

    @Test
    void shouldThrowDuplicateBookExceptionWhenIsbnExists() {
        when(bookRepository.findByIsbn("978-0134685991")).thenReturn(Optional.of(book));

        assertThrows(DuplicateBookException.class, () -> bookService.createBook(requestDTO));
    }

    @Test
    void shouldThrowDuplicateBookExceptionWhenIsbnExists_bulkCreate() {
        // TODO: Add test logic for duplicate ISBN in bulk create
    }

    @Test
    void shouldReturnEmptyPageWhenNoBooksFound() {
        // TODO: Setup findAll(Pageable) to return empty and verify response
    }

    @Test
    void shouldReturnEmptyListWhenNoBooksExist_unpaged() {
        // TODO: Setup findAll() to return empty and assert empty list returned
    }

    @Test
    void shouldPreserveExistingIsbnDuringUpdate() {
        // TODO: Ensure ISBN field remains unchanged after update
    }
    @Test
    void testDeleteBook_Successful() {
        Book book = Book.builder()
                .id("123")
                .isbn("9781234567890")
                .title("Clean Code")
                .build();

        when(bookRepository.findById("123")).thenReturn(Optional.of(book));
        doNothing().when(bookRepository).deleteById("123");

        DeleteResponseDTO response = bookService.deleteBook("123");

        assertEquals("Book deleted successfully", response.getMessage());
        assertEquals("123", response.getDeletedBookId());
        assertEquals("9781234567890", response.getIsbn());
        assertEquals("Clean Code", response.getTitle());

        verify(bookRepository, times(1)).deleteById("123");
    }
    @Test
    void testDeleteBook_NotFound_ThrowsException() {
        when(bookRepository.findById("invalid-id")).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class, () -> bookService.deleteBook("invalid-id"));
    }


}
