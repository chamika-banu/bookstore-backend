package com.mycompany.bookstore.service;

import com.mycompany.bookstore.exception.*;
import com.mycompany.bookstore.model.Book;
import com.mycompany.bookstore.model.CartItem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

public class CartService {
    // Collection of carts of each customerId
    private final Map<Long, List<CartItem>> carts = new HashMap<>();

    // Initialize singleton instance
    private static final CartService instance = new CartService();

    public static CartService getInstance() {
        return instance;
    }

    // Get cart
    public List<CartItem> getCart(Long customerId) {
        checkIfCustomerExists(customerId);
        return carts.get(customerId);
    }

    // Add item
    public List<CartItem> addItem(Long customerId, CartItem item) {
        checkIfCustomerExists(customerId);
        List<CartItem> cart = carts.computeIfAbsent(customerId, k -> new ArrayList<>());
        
        if(item.getBookId() == null) {
            throw new InvalidInputException("Cannot add item to cart. No ID found");
        }

        if (item.getQuantity() <= 0) {
            throw new InvalidInputException("Quantity must be greater than 0.");
        }

        Book selectedBook = getBookOrThrowError(item.getBookId());

        // Check if the item is already in the cart
        boolean itemFound = false;
        for (CartItem cartItem : cart) {
            if (cartItem.getBookId().equals(item.getBookId())) {

                int newTotalQuantity = cartItem.getQuantity() + item.getQuantity();

                if (newTotalQuantity > selectedBook.getStockQuantity()) {
                    throw new OutOfStockException("Only " + selectedBook.getStockQuantity() + " item(s) left in stock for book ID " + item.getBookId() + ". Cannot exceed available stock.");
                }

                cartItem.setQuantity(cartItem.getQuantity() + item.getQuantity());
                itemFound = true;
                break;
            }
        }

        // If the item is not found, add it to the cart with the provided quantity
        if (!itemFound) {
            item.setTitle(selectedBook.getTitle());
            cart.add(item);
        }

        return cart;
    }

    // Update item
    public List<CartItem> updateItem(Long customerId, Long bookId, CartItem item) {
        checkIfCustomerExists(customerId);
        List<CartItem> cart = carts.get(customerId);

        if (cart == null || cart.isEmpty()) {
            throw new CartNotFoundException("Cart is empty. Add items.");
        }

        if (item.getQuantity() <= 0) {
            throw new InvalidInputException("Quantity must be greater than 0.");
        }

        Book selectedBook = getBookOrThrowError(bookId);

        boolean itemFound = false;

        for (CartItem cartItem : cart) {
            if (cartItem.getBookId().equals(bookId)) {

                if (item.getQuantity() > selectedBook.getStockQuantity()) {
                    throw new OutOfStockException("Only " + selectedBook.getStockQuantity() + " item(s) left in stock for book ID " + bookId + ". Cannot exceed available stock.");
                }
                cartItem.setQuantity(item.getQuantity());
                itemFound = true;
                break;
            }
        }

        if (!itemFound) {
            throw new BookNotFoundInCartException("Book with ID " + bookId + " not found in cart.");
        }

        return cart;
    }

    // Remove item
    public List<CartItem> removeItem(Long customerId, Long bookId) {
        checkIfCustomerExists(customerId);

        List<CartItem> cart = carts.get(customerId);

        if (cart == null || cart.isEmpty()) {
            throw new CartNotFoundException("Cart is empty");
        }

        getBookOrThrowError(bookId);

        boolean itemFound = false;

        for (CartItem cartItem : cart) {
            if (cartItem.getBookId().equals(bookId)) {
                itemFound = true;
                break;
            }
        }

        if (!itemFound) {
            throw new BookNotFoundInCartException("Book with ID " + bookId + " not found in cart.");
        }

        cart.removeIf(item -> item.getBookId().equals(bookId));

        return cart;
    }

    public void checkIfCustomerExists(Long customerId) {
        boolean customerNotFound = CustomerService.getInstance()
                .getAllCustomers()
                .stream()
                .noneMatch(customer -> customer.getId().equals(customerId));

        if (customerNotFound) {
            throw new CustomerNotFoundException("Customer with ID " + customerId + " not found.");
        }
    }

    private Book getBookOrThrowError(Long bookId) {
        return BookService.getInstance()
                .getAllBooks()
                .stream()
                .filter(book -> book.getId().equals(bookId))
                .findFirst()
                .orElseThrow(() -> new BookNotFoundException("Book with ID " + bookId + " not found."));
    }

}

