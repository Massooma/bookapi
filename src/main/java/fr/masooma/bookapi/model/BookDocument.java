package fr.masooma.bookapi.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "books")
public class BookDocument {

    @Id
    private Long id;

    private String isbn13;
    private String isbn10;
    private String title;
    private String authors;
    private String categories;
    private String description;
    private Integer publishedYear;
    private Double averageRating;

    public BookDocument() {
    }

    public BookDocument(
            Long id,
            String isbn13,
            String isbn10,
            String title,
            String authors,
            String categories,
            String description,
            Integer publishedYear,
            Double averageRating) {

        this.id = id;
        this.isbn13 = isbn13;
        this.isbn10 = isbn10;
        this.title = title;
        this.authors = authors;
        this.categories = categories;
        this.description = description;
        this.publishedYear = publishedYear;
        this.averageRating = averageRating;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIsbn13() {
        return isbn13;
    }

    public void setIsbn13(String isbn13) {
        this.isbn13 = isbn13;
    }

    public String getIsbn10() {
        return isbn10;
    }

    public void setIsbn10(String isbn10) {
        this.isbn10 = isbn10;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthors() {
        return authors;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getPublishedYear() {
        return publishedYear;
    }

    public void setPublishedYear(Integer publishedYear) {
        this.publishedYear = publishedYear;
    }

    public Double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(Double averageRating) {
        this.averageRating = averageRating;
    }
}