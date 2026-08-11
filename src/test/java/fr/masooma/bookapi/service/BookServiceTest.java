package fr.masooma.bookapi.service;

import fr.masooma.bookapi.exception.BookNotFoundException;
import fr.masooma.bookapi.model.Book;
import fr.masooma.bookapi.repository.BookDocumentRepository;
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

    @Mock
    private BookDocumentRepository bookDocumentRepository;

    @InjectMocks
    private BookService bookService;

    @Test
    void shouldSaveBook() {

        Book book = new Book(
                "9780002005883",
                "0002005883",
                "Gilead",
                "Marilynne Robinson",
                "Fiction",
                "A novel about John Ames, a preacher in Gilead, Iowa.",
                2004,
                3.85
        );

        when(bookRepository.save(book)).thenReturn(book);

        Book result = bookService.saveBook(book);

        assertEquals("9780002005883", result.getIsbn13());
        assertEquals("0002005883", result.getIsbn10());
        assertEquals("Gilead", result.getTitle());
        assertEquals("Marilynne Robinson", result.getAuthors());
        assertEquals("Fiction", result.getCategories());
        assertEquals(2004, result.getPublishedYear());
        assertEquals(3.85, result.getAverageRating());

        verify(bookRepository).save(book);
        verify(bookDocumentRepository).save(any());
    }

    @Test
    void shouldGetBookById() {

        Book book = new Book(
                "9780002005883",
                "0002005883",
                "Gilead",
                "Marilynne Robinson",
                "Fiction",
                "A novel about John Ames, a preacher in Gilead, Iowa.",
                2004,
                3.85
        );

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        Book result = bookService.getById(1L);

        assertEquals("Gilead", result.getTitle());
        assertEquals("Marilynne Robinson", result.getAuthors());
        assertEquals("9780002005883", result.getIsbn13());
        assertEquals(2004, result.getPublishedYear());
        assertEquals(3.85, result.getAverageRating());

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

        Book existingBook = new Book(
                "9780002005883",
                "0002005883",
                "Gilead",
                "Marilynne Robinson",
                "Fiction",
                "A novel about John Ames.",
                2004,
                3.85
        );

        Book newBook = new Book(
                "9780002261982",
                "0002261987",
                "Spider's Web",
                "Charles Osborne;Agatha Christie",
                "Detective and mystery stories",
                "A mystery novel adapted from Agatha Christie's play.",
                2000,
                3.83
        );

        when(bookRepository.findById(1L)).thenReturn(Optional.of(existingBook));
        when(bookRepository.save(existingBook)).thenReturn(existingBook);

        Book result = bookService.updateBook(1L, newBook);

        assertEquals("9780002261982", result.getIsbn13());
        assertEquals("0002261987", result.getIsbn10());
        assertEquals("Spider's Web", result.getTitle());
        assertEquals(
                "Charles Osborne;Agatha Christie",
                result.getAuthors()
        );
        assertEquals(
                "Detective and mystery stories",
                result.getCategories()
        );
        assertEquals(2000, result.getPublishedYear());
        assertEquals(3.83, result.getAverageRating());

        verify(bookRepository).findById(1L);
        verify(bookRepository).save(existingBook);
        verify(bookDocumentRepository).save(any());
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistingBook() {

        Book newBook = new Book(
                "9780002261982",
                "0002261987",
                "Spider's Web",
                "Charles Osborne;Agatha Christie",
                "Detective and mystery stories",
                "A mystery novel.",
                2000,
                3.83
        );

        when(bookRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(
                BookNotFoundException.class,
                () -> bookService.updateBook(999L, newBook)
        );

        verify(bookRepository).findById(999L);
        verify(bookRepository, never()).save(any());
        verify(bookDocumentRepository, never()).save(any());
    }

    @Test
    void shouldDeleteBook() {

        Book book = new Book(
                "9780002005883",
                "0002005883",
                "Gilead",
                "Marilynne Robinson",
                "Fiction",
                "A novel about John Ames.",
                2004,
                3.85
        );

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        bookService.deleteBook(1L);

        verify(bookRepository).findById(1L);
        verify(bookRepository).delete(book);
        verify(bookDocumentRepository).deleteById(1L);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistingBook() {

        when(bookRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(
                BookNotFoundException.class,
                () -> bookService.deleteBook(999L)
        );

        verify(bookRepository).findById(999L);
        verify(bookRepository, never()).delete(any());
        verify(bookDocumentRepository, never()).deleteById(any());
    }

    @Test
    void shouldGetAllBooks() {

        Book book1 = new Book(
                "9780002005883",
                "0002005883",
                "Gilead",
                "Marilynne Robinson",
                "Fiction",
                "A novel about John Ames.",
                2004,
                3.85
        );

        Book book2 = new Book(
                "9780002261982",
                "0002261987",
                "Spider's Web",
                "Charles Osborne;Agatha Christie",
                "Detective and mystery stories",
                "A mystery novel adapted from Agatha Christie's play.",
                2000,
                3.83
        );

        List<Book> books = List.of(book1, book2);

        when(bookRepository.findAll()).thenReturn(books);

        List<Book> result = bookService.getAllBooks();

        assertEquals(2, result.size());

        assertEquals("Gilead", result.get(0).getTitle());
        assertEquals("Marilynne Robinson", result.get(0).getAuthors());
        assertEquals("9780002005883", result.get(0).getIsbn13());

        assertEquals("Spider's Web", result.get(1).getTitle());
        assertEquals(
                "Charles Osborne;Agatha Christie",
                result.get(1).getAuthors()
        );
        assertEquals("9780002261982", result.get(1).getIsbn13());

        verify(bookRepository).findAll();
    }
}
