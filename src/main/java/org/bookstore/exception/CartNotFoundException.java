package org.bookstore.exception;

public class CartNotFoundException extends RuntimeException {
    public CartNotFoundException(String error) {
        super(error);
    }
}
