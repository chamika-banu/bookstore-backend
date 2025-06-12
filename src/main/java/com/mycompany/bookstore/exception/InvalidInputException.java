package com.mycompany.bookstore.exception;

public class InvalidInputException extends RuntimeException {
    public InvalidInputException(String error) {
        super(error);
    }
}
