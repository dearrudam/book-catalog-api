package io.github.dearrudam.bookstoreapi.books;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.ISBN;

public record BookRequest(
        @NotNull
        @ISBN(type = ISBN.Type.ISBN_13)
        String isbn,
        @NotEmpty
        String title,
        @NotEmpty
        String description) {

    public Book toBook() {
        return new Book(this.isbn, this.title, this.description);
    }
}
