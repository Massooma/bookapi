package fr.masooma.bookapi.controller;

import fr.masooma.bookapi.model.Book;
import fr.masooma.bookapi.service.BookService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService){
        this.bookService = bookService;
    }

    @GetMapping
    public List<Book> getBooks(){
        return bookService.getAllBooks();
    }

    @PostMapping
    public Book createBook(@RequestBody Book book){
        return bookService.saveBook(book);
    }

    @DeleteMapping("/{id}")
    public void deleteBook(@PathVariable Long id){
        bookService.deleteBook(id);
    }

    @PutMapping("/{id}")
    public Book updateBook(@PathVariable Long id, @RequestBody Book newBook){
        return bookService.updateBook(id,newBook);
    }

    @GetMapping("/{id}")
    public Book getBookById(@PathVariable Long id){
        return bookService.getById(id);
    }
}

