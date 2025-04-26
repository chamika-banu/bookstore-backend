package org.bookstore.exception;

public class OutOfStockException extends RuntimeException {
    public OutOfStockException(String error) {
        super(error);
    }
}
