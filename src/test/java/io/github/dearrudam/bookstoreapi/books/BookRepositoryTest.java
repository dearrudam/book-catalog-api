package io.github.dearrudam.bookstoreapi.books;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class BookRepositoryTest {

    @Autowired
    private BookRepository repository;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
    }

    @Test
    @DisplayName("must save a valid book")
    void test_01() {

        Book book = new Book("978-85-7608-207-1", "Java Concurrency in Practice", "Multi-threading programming with Java by Brian Goetz");

        repository.save(book);

        assertEquals(1, repository.findAll().size());
    }

    @Test
    @DisplayName("must get a book by isbn")
    void test_02() {
        Book book1 = new Book("978-85-7608-207-1", "Java Concurrency in Practice", "Multi-threading programming with Java by Brian Goetz");

        repository.save(book1);

        Optional<Book> optionalBook = repository.findByIsbn(book1.getIsbn());

        assertTrue(optionalBook.isPresent());
        assertEquals(book1, optionalBook.get());
    }


    @Test
    @DisplayName("should not save a book with isbn already registered")
    void test_03() {

        String isbn = "978-85-7608-207-1";
        Book book1 = new Book(isbn, "Java Concurrency in Practice", "Multi-threading programming with Java by Brian Goetz");

        repository.save(book1);

        assertThrows(DataIntegrityViolationException.class, () -> {
            Book book2 = new Book(isbn, "Domain Drive Design", "DDD by Eric Evans");
            repository.save(book2);
        });

    }
}