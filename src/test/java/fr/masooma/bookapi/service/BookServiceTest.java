package fr.masooma.bookapi.service;

import fr.masooma.bookapi.exception.BookNotFoundException;
import fr.masooma.bookapi.model.Book;
import fr.masooma.bookapi.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    @Test
    void shouldSaveBook() {

        Book book = new Book();
        book.setTitle("Clean Architecture");
        book.setAuthors("Robert C. Martin");

        when(bookRepository.save(book)).thenReturn(book);

        Book result = bookService.saveBook(book);

        assertEquals("Clean Architecture", result.getTitle());
        assertEquals("Robert C. Martin", result.getAuthors());

        verify(bookRepository).save(book);
    }

    @Test
    void shouldGetBookById() {

        Book book = new Book();
        book.setTitle("Clean Code");
        book.setAuthors("Robert C. Martin");

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        Book result = bookService.getById(1L);

        assertEquals("Clean Code", result.getTitle());
        assertEquals("Robert C. Martin", result.getAuthors());

        verify(bookRepository).findById(1L);
    }

    @Test
    void shouldThrowExceptionWhenBookDoesNotExist() {

        when(bookRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(
                BookNotFoundException.class,
                () -> bookService.getById(999L)
        );

        verify(bookRepository).findById(999L);
    }

    @Test
    void shouldUpdateBook() {

        Book existingBook = new Book();
        existingBook.setTitle("Old Title");
        existingBook.setAuthors("Old Author");

        Book newBook = new Book();
        newBook.setTitle("New Title");
        newBook.setAuthors("New Author");

        when(bookRepository.findById(1L)).thenReturn(Optional.of(existingBook));
        when(bookRepository.save(existingBook)).thenReturn(existingBook);

        Book result = bookService.updateBook(1L, newBook);

        assertEquals("New Title", result.getTitle());
        assertEquals("New Author", result.getAuthors());

        verify(bookRepository).findById(1L);
        verify(bookRepository).save(existingBook);
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistingBook() {

        Book newBook = new Book();
        newBook.setTitle("New Title");
        newBook.setAuthors("New Author");

        when(bookRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(
                BookNotFoundException.class,
                () -> bookService.updateBook(999L, newBook)
        );

        verify(bookRepository).findById(999L);
        verify(bookRepository, never()).save(any());
    }

    @Test
    void shouldDeleteBook() {

        bookService.deleteBook(1L);

        verify(bookRepository).deleteById(1L);
    }

    @Test
    void shouldGetAllBooks() {

        Book book1 = new Book();
        book1.setTitle("Clean Code");
        book1.setAuthors("Robert C. Martin");

        Book book2 = new Book();
        book2.setTitle("Effective Java");
        book2.setAuthors("Joshua Bloch");

        List<Book> books = List.of(book1, book2);

        when(bookRepository.findAll()).thenReturn(books);

        List<Book> result = bookService.getAllBooks();

        assertEquals(2, result.size());
        assertEquals("Clean Code", result.get(0).getTitle());
        assertEquals("Effective Java", result.get(1).getTitle());

        verify(bookRepository).findAll();
    }
}