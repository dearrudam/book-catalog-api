package io.github.dearrudam.bookstoreapi.books;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.ISBN;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/books")
public class BookController {

    private final BookRepository bookRepository;

    public BookController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @PostMapping
    @Transactional
    public ResponseEntity<?> create(@RequestBody
                                    @Valid
                                    BookRequest request,
                                    UriComponentsBuilder uriBuilder) {
        if (bookRepository.findByIsbn(request.isbn()).isPresent()) {
            throw new IllegalArgumentException("ISBN is registered already by other book");
        }
        Book newBook = request.toBook();
        bookRepository.save(newBook);
        URI location = uriBuilder.path("/api/v1/books/{isbn}")
                .buildAndExpand(newBook.getIsbn())
                .toUri();
        return ResponseEntity.created(location)
                .build();
    }

    @GetMapping(path = "{isbn}")
    public ResponseEntity<?> findBookByIsbn(@PathVariable
                                       @NotNull
                                       @ISBN(type = ISBN.Type.ISBN_13)
                                       String isbn) {
        Optional<Book> book = bookRepository.findByIsbn(isbn);
        if (book.isPresent()) {
            return ResponseEntity.of(book.map(BookResponse::new));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping(path = "{isbn}")
    public ResponseEntity<?> deleteBookByIsbn(@PathVariable
                                              @NotNull
                                              @ISBN(type = ISBN.Type.ISBN_13)
                                              String isbn) {
        Optional<Book> book = bookRepository.findByIsbn(isbn);
        if (book.isPresent()) {
            bookRepository.deleteById(book.get().getId());
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }


    @GetMapping
    public ResponseEntity<?> listAll() {
        List<Book> books = bookRepository.findAll();
        return ResponseEntity.ok(
                books.stream()
                        .map(BookResponse::new)
                        .toList()
        );
    }
}
