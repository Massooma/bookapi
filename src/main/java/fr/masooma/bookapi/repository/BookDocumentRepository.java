package fr.masooma.bookapi.repository;

import fr.masooma.bookapi.model.BookDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface BookDocumentRepository extends ElasticsearchRepository<BookDocument, Long> {
}