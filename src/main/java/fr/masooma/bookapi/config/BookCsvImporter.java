package fr.masooma.bookapi.config;

import fr.masooma.bookapi.model.Book;
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

    public BookCsvImporter(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        ClassPathResource resource = new ClassPathResource("books.csv");

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8));
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

                Integer publishedYear = parseInteger(record.get("published_year"));
                Double averageRating = parseDouble(record.get("average_rating"));

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

                bookRepository.save(book);
            }
        }
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