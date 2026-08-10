package fr.masooma.bookapi.service;

import fr.masooma.bookapi.exception.BookNotFoundException;
import fr.masooma.bookapi.model.Book;
import fr.masooma.bookapi.model.BookDocument;
import fr.masooma.bookapi.repository.BookDocumentRepository;
import fr.masooma.bookapi.repository.BookRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final BookDocumentRepository bookDocumentRepository;

    public BookService(
            BookRepository bookRepository,
            BookDocumentRepository bookDocumentRepository) {

        this.bookRepository = bookRepository;
        this.bookDocumentRepository = bookDocumentRepository;
    }

    // convert a book to a bookDocument
    private BookDocument toDocument(Book book) {
        return new BookDocument(
                book.getId(),
                book.getIsbn13(),
                book.getIsbn10(),
                book.getTitle(),
                book.getAuthors(),
                book.getCategories(),
                book.getDescription(),
                book.getPublishedYear(),
                book.getAverageRating()
        );
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Book saveBook(Book book) {
        Book savedBook = bookRepository.save(book);
        BookDocument document = toDocument(savedBook);
        bookDocumentRepository.save(document);
        return savedBook;
    }

    public void deleteBook(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));

        bookRepository.delete(book);
        bookDocumentRepository.deleteById(id);
    }

    public Book updateBook(Long id, Book newBook) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));

        book.setIsbn13(newBook.getIsbn13());
        book.setIsbn10(newBook.getIsbn10());
        book.setTitle(newBook.getTitle());
        book.setAuthors(newBook.getAuthors());
        book.setCategories(newBook.getCategories());
        book.setDescription(newBook.getDescription());
        book.setPublishedYear(newBook.getPublishedYear());
        book.setAverageRating(newBook.getAverageRating());

        Book savedBook = bookRepository.save(book);

        bookDocumentRepository.save(toDocument(savedBook));

        return savedBook;
    }

    public Book getById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));
    }

    public Page<BookDocument> searchBooks(String query, Pageable pageable) {
        return bookDocumentRepository
                .search(query, pageable);
    }
}