package fr.masooma.bookapi.exception;

public class BookNotFoundException extends RuntimeException{

    public BookNotFoundException(Long id) {
        super("Livre introuvable : " + id);
    }

}
