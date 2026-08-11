package fr.masooma.bookapi.repository;

import fr.masooma.bookapi.model.BookDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface BookDocumentRepository extends ElasticsearchRepository<BookDocument, Long> {

    @Query("""
    {
      "multi_match": {
        "query": "?0",
        "fields": [
          "title^3",
          "authors^2",
          "categories^1.5",
          "description"
        ]
      }
    }
    """)
    Page<BookDocument> search(String query, Pageable pageable);
}