package com.mycompany.bookstore.exception;

public class AuthorNotFoundException extends RuntimeException{
    public AuthorNotFoundException(String error) {
        super(error);
    }
}
