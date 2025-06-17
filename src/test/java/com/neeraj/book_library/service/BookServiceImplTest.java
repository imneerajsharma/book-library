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

        BookPageResponse response = bookService.getBooksPaginated(0, 10, "asc", "title");

        assertEquals(1, response.getTotalElements());
        assertEquals("Effective Java", response.getBooks().get(0).getTitle());
    }

    @Test
    void shouldReturnAllBooksUnpaged() {
        when(bookRepository.findAll()).thenReturn(List.of(book));
        when(modelMapper.map(book, BookResponseDTO.class)).thenReturn(responseDTO);

        List<BookResponseDTO> books = bookService.getAllBooks();

        assertEquals(1, books.size());
        assertEquals("Effective Java", books.get(0).getTitle());
    }
}
