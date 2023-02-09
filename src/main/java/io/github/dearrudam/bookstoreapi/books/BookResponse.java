package io.github.dearrudam.bookstoreapi.books;

public record BookResponse(String isbn, String title, String description) {

    public BookResponse(Book book) {
        this(book.getIsbn(), book.getTitle(), book.getDescription());
    }
}
