package com.mycompany.bookstore.exception;

public class CustomerNotFoundException extends RuntimeException {
    public CustomerNotFoundException(String error) {
        super(error);
    }
}
