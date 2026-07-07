package fr.masooma.bookapi;

import fr.masooma.bookapi.model.Book;
import fr.masooma.bookapi.repository.BookRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BookapiApplication {

	@Bean
	CommandLineRunner init(BookRepository repository) {
		return args -> {
			repository.save(new Book("Clean Code", "Robert C. Martin"));
			repository.save(new Book("Effective Java", "Joshua Bloch"));
		};
	}
	public static void main(String[] args) {

		SpringApplication.run(BookapiApplication.class, args);
	}

}
