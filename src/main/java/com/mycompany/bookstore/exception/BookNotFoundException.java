package com.mycompany.bookstore.exception;

public class BookNotFoundException extends RuntimeException {
    public BookNotFoundException(String error) {
        super(error);
    }
}
