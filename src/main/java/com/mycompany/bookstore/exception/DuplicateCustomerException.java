package com.mycompany.bookstore.exception;

public class DuplicateCustomerException extends RuntimeException {
    public DuplicateCustomerException(String error) {
        super(error);
    }
}
