package fr.masooma.bookapi.service;

import fr.masooma.bookapi.model.Book;
import fr.masooma.bookapi.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import fr.masooma.bookapi.exception.BookNotFoundException;

@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository){
        this.bookRepository = bookRepository;
    }
    public List<Book> getAllBooks(){
        return bookRepository.findAll();
    }

    public Book saveBook(Book book){
        return bookRepository.save(book);
    }

    public void deleteBook(Long id){
        bookRepository.deleteById(id);
    }

    public Book updateBook(Long id, Book newBook){
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));
        book.setTitle(newBook.getTitle());
        book.setAuthors(newBook.getAuthors());
        return bookRepository.save(book);
    }

    public Book getById(Long id){
        return  bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException(id));
    }
}
