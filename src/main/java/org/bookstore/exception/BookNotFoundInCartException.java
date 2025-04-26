package org.bookstore.exception;

public class BookNotFoundInCartException extends RuntimeException {
    public BookNotFoundInCartException(String error) {
        super(error);
    }
}
