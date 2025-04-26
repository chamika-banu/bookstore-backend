package org.bookstore.exception;

public class AuthorNotFoundException extends RuntimeException{
    public AuthorNotFoundException(String error) {
        super(error);
    }
}
