package fr.masooma.bookapi.repository;

import fr.masooma.bookapi.model.BookDocument;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;
public interface BookDocumentRepository extends ElasticsearchRepository<BookDocument, Long> {

    @Query("""
        {
          "multi_match": {
            "query": "?0",
            "fields": [
              "title",
              "authors",
              "categories",
              "description"
            ]
          }
        }
        """)
    List<BookDocument> search(String query);
}