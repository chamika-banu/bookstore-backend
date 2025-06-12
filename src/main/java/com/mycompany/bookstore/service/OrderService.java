package com.mycompany.bookstore.service;

import com.mycompany.bookstore.exception.BookNotFoundException;
import com.mycompany.bookstore.exception.OutOfStockException;
import com.mycompany.bookstore.exception.CartNotFoundException;
import com.mycompany.bookstore.model.CartItem;
import com.mycompany.bookstore.model.Book;
import com.mycompany.bookstore.model.Order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class OrderService {
    private static final AtomicLong idCounter = new AtomicLong(0);

    private final Map<Long, List<Order>> ordersByCustomer = new HashMap<>();

    private static final OrderService instance = new OrderService();

    private final CartService cartService = CartService.getInstance();

    public static OrderService getInstance() {
        return instance;
    }

    // Create order
    public Order createOrder(Long customerId) {
        cartService.checkIfCustomerExists(customerId);

        List<CartItem> cart = cartService.getCart(customerId);

        if (cart == null || cart.isEmpty()) {
            throw new CartNotFoundException("Cart is empty. Cannot place order.");
        }

        BookService bookService = BookService.getInstance();

        double total = 0.0;

        for (CartItem item : cart) {                        
            Long bookId = item.getBookId();
            int quantity = item.getQuantity();

            // Get book
            Book book = bookService.getAllBooks()
                    .stream()
                    .filter(b -> b.getId().equals(bookId))
                    .findFirst()
                    .orElseThrow(() -> new BookNotFoundException("Book with ID " + bookId + " not found."));

            if (book.getStockQuantity() < quantity) {
                throw new OutOfStockException("Not enough stock for book ID " + bookId);
            }
            // Reduce stock
            book.setStockQuantity(book.getStockQuantity() - quantity);

            // Add to total
            total += book.getPrice() * quantity;
        }

        Order newOrder = new Order();
        newOrder.setId(idCounter.incrementAndGet());
        newOrder.setCustomerId(customerId);
        newOrder.setItems(new ArrayList<>(cart));
        newOrder.setTotalAmount(total);

        ordersByCustomer.computeIfAbsent(customerId, k -> new ArrayList<>()).add(newOrder);

        // Clear cart after ordering
        cart.clear();

        return newOrder;
    }

    // Get orders by customer
    public List<Order> getOrders(Long customerId) {
        cartService.checkIfCustomerExists(customerId);
        return ordersByCustomer.getOrDefault(customerId, new ArrayList<>());
    }

    // Get order by orderId
    public Order getOrder(Long customerId, Long orderId) {
        cartService.checkIfCustomerExists(customerId);
        return ordersByCustomer.getOrDefault(customerId, new ArrayList<>())
                .stream()
                .filter(o -> o.getId().equals(orderId))
                .findFirst()
                .orElse(null);
    }
}
