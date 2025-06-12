package com.mycompany.bookstore.exception;

public class DuplicateAuthorException extends RuntimeException {
    public DuplicateAuthorException(String error) {
        super(error);
    }
}
