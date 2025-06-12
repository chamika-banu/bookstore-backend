package com.mycompany.bookstore.exception;

public class BookNotFoundInCartException extends RuntimeException {
    public BookNotFoundInCartException(String error) {
        super(error);
    }
}
