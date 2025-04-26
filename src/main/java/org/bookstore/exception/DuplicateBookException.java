package org.bookstore.exception;

public class DuplicateBookException extends RuntimeException {
    public DuplicateBookException(String error) {
        super(error);
    }
}
