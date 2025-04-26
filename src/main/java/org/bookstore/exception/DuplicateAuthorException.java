package org.bookstore.exception;

public class DuplicateAuthorException extends RuntimeException {
    public DuplicateAuthorException(String error) {
        super(error);
    }
}
