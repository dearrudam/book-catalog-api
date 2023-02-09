package io.github.dearrudam.bookstoreapi.books;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.ISBN;

import java.util.Objects;

@Entity
@Table(
        uniqueConstraints = {
                @UniqueConstraint(name = "uc_isbn", columnNames = "isbn")
        }
)
public class Book {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @ISBN(type = ISBN.Type.ISBN_13)
    private String isbn;

    @NotEmpty
    private String title;

    @NotEmpty
    private String description;

    /**
     * @deprecated Exclusive use for JPA Implementation
     */
    @Deprecated
    public Book() {
    }

    public Book(@NotNull
                @ISBN(type = ISBN.Type.ISBN_13) String isbn,
                @NotEmpty String title,
                @NotEmpty String description) {
        this.isbn = isbn;
        this.title = title;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Objects.equals(id, book.id)
                && Objects.equals(isbn, book.isbn)
                && Objects.equals(title, book.title)
                && Objects.equals(description, book.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, isbn, title, description);
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", isbn='" + isbn + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
