package fr.masooma.bookapi.controller;

import fr.masooma.bookapi.model.Book;
import fr.masooma.bookapi.model.BookDocument;
import fr.masooma.bookapi.service.BookService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public List<Book> getBooks() {
        return bookService.getAllBooks();
    }

    @GetMapping("/search")
    public Page<BookDocument> searchBooks(
            @RequestParam String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "title") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {

        if (page < 0) {
            throw new IllegalArgumentException("Page must be >= 0");
        }

        if (size < 1 || size > 100) {
            throw new IllegalArgumentException("Size must be between 1 and 100");
        }

        List<String> allowedSortFields = List.of(
                "title",
                "publishedYear",
                "averageRating",
                "authors"
        );

        if (!allowedSortFields.contains(sortBy)) {
            throw new IllegalArgumentException(
                    "Invalid sort field. Allowed values: " + allowedSortFields
            );
        }

        Sort.Direction sortDirection;

        try {
            sortDirection = Sort.Direction.fromString(direction);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                    "Invalid sort direction. Use 'asc' or 'desc'."
            );
        }

        String elasticsearchSortField = switch (sortBy) {
            case "title", "authors" -> sortBy + ".keyword";
            case "publishedYear", "averageRating" -> sortBy;
            default -> throw new IllegalArgumentException(
                    "Invalid sort field"
            );
        };

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(sortDirection, elasticsearchSortField)
        );

        return bookService.searchBooks(q, pageable);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Book createBook(@Valid @RequestBody Book book) {
        return bookService.saveBook(book);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
    }

    @PutMapping("/{id}")
    public Book updateBook(
            @PathVariable Long id,
            @Valid @RequestBody Book newBook) {

        return bookService.updateBook(id, newBook);
    }

    @GetMapping("/{id}")
    public Book getBookById(@PathVariable Long id) {
        return bookService.getById(id);
    }
}