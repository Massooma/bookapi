package fr.masooma.bookapi.config;

import fr.masooma.bookapi.model.Book;
import fr.masooma.bookapi.model.BookDocument;
import fr.masooma.bookapi.repository.BookDocumentRepository;
import fr.masooma.bookapi.repository.BookRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

@Component
public class BookCsvImporter implements CommandLineRunner {

    private final BookRepository bookRepository;
    private final BookDocumentRepository bookDocumentRepository;

    public BookCsvImporter(
            BookRepository bookRepository,
            BookDocumentRepository bookDocumentRepository) {

        this.bookRepository = bookRepository;
        this.bookDocumentRepository = bookDocumentRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        // H2 persistante
        if (bookRepository.count() > 0) {
            System.out.println("Books already imported. Skipping CSV import.");
            return;
        }

        System.out.println("Importing books from CSV...");

        ClassPathResource resource = new ClassPathResource("books.csv");

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        resource.getInputStream(),
                        StandardCharsets.UTF_8));
             CSVParser parser = CSVFormat.DEFAULT.builder()
                     .setHeader()
                     .setSkipHeaderRecord(true)
                     .setIgnoreEmptyLines(true)
                     .build()
                     .parse(reader)) {

            for (CSVRecord record : parser) {

                String isbn13 = record.get("isbn13");
                String isbn10 = record.get("isbn10");
                String title = record.get("title");
                String authors = record.get("authors");
                String categories = record.get("categories");
                String description = record.get("description");

                Integer publishedYear =
                        parseInteger(record.get("published_year"));

                Double averageRating =
                        parseDouble(record.get("average_rating"));

                Book book = new Book(
                        isbn13,
                        isbn10,
                        title,
                        authors,
                        categories,
                        description,
                        publishedYear,
                        averageRating
                );

                /*
                 * Sauvegarde dans H2
                 */
                Book savedBook = bookRepository.save(book);

                /*
                 * Création du document Elasticsearch
                 */
                BookDocument document = new BookDocument(
                        savedBook.getId(),
                        savedBook.getIsbn13(),
                        savedBook.getIsbn10(),
                        savedBook.getTitle(),
                        savedBook.getAuthors(),
                        savedBook.getCategories(),
                        savedBook.getDescription(),
                        savedBook.getPublishedYear(),
                        savedBook.getAverageRating()
                );

                /*
                 * Indexation dans Elasticsearch
                 */
                bookDocumentRepository.save(document);
            }
        }

        System.out.println(
                "Import completed. Books in H2: "
                        + bookRepository.count()
        );

        System.out.println(
                "Books in Elasticsearch: "
                        + bookDocumentRepository.count()
        );
    }

    private Integer parseInteger(String value) {

        if (value == null || value.isBlank()) {
            return null;
        }

        try {
            return (int) Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private Double parseDouble(String value) {

        if (value == null || value.isBlank()) {
            return null;
        }

        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}