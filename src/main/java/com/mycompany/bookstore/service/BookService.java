package com.mycompany.bookstore.service;

import com.mycompany.bookstore.exception.BookNotFoundException;
import com.mycompany.bookstore.exception.DuplicateBookException;
import com.mycompany.bookstore.exception.InvalidInputException;
import com.mycompany.bookstore.model.Author;
import com.mycompany.bookstore.model.Book;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class BookService {
    private static final AtomicLong idCounter = new AtomicLong(0);

    // Collection for books
    private static final Map<Long, Book> books = new HashMap<>();

    // Initialize singleton instance
    private static final BookService instance = new BookService();

    private BookService() {
        // Sample books to simulate some books in memory
        books.put(idCounter.incrementAndGet(), new Book(idCounter.get(), "Harry Potter and the Philosopher's Stone", 1L, "9780747532699", 1997, 20.0, 10));
        books.put(idCounter.incrementAndGet(), new Book(idCounter.get(), "Harry Potter and the Chamber of Secrets", 1L, "9780747538493", 1998, 22.0, 8));
        books.put(idCounter.incrementAndGet(), new Book(idCounter.get(), "A Game of Thrones", 2L, "9780553103540", 1996, 25.0, 8));
    }

    public static BookService getInstance() {
        return instance;
    }

    // Get all books
    public List<Book> getAllBooks() {
        List<Book> bookList = new ArrayList<>();

        for (Book book : books.values()) {
            Author author = AuthorService.getInstance().getAuthorById(book.getAuthorId());
            // Populate the author name
            book.setAuthorName(author.getName());
            bookList.add(book);
        }
        return bookList;
    }

    // Get book by id
    public Book getBookById(Long id) {
        Book book = books.get(id);
        if(book == null) {
            throw new BookNotFoundException("Book with ID " + id + " not found.");
        }

        Author author = AuthorService.getInstance().getAuthorById(book.getAuthorId());
        book.setAuthorName(author.getName());

        return book;
    }

    // Create book
    public Book createBook(Book book) {
        validateBookInput(book);

        for(Book currentBook : books.values()) {
            if(currentBook.getTitle().equalsIgnoreCase(book.getTitle())) {
                throw new DuplicateBookException("Book with title '" + book.getTitle() + "' already exists.");
            }
        }

        Long id = idCounter.incrementAndGet();
        book.setId(id);

        Author author = AuthorService.getInstance().getAuthorById(book.getAuthorId());
        book.setAuthorName(author.getName());

        books.put(id, book);
        return book;
    }

    // Update book
    public Book updateBook(Long id, Book updatedBook) {
        validateBookInput(updatedBook);

        Book existingBook = books.get(id);

        if(existingBook == null) {
            throw new BookNotFoundException("Book with ID " + id + " not found.");
        }

        // Update the existing book's details
        existingBook.setTitle(updatedBook.getTitle());
        existingBook.setAuthorId(updatedBook.getAuthorId());
        existingBook.setIsbn(updatedBook.getIsbn());
        existingBook.setPublicationYear(updatedBook.getPublicationYear());
        existingBook.setPrice(updatedBook.getPrice());
        existingBook.setStockQuantity(updatedBook.getStockQuantity());

        // Update the author name based on new authorId
        Author author = AuthorService.getInstance().getAuthorById(updatedBook.getAuthorId());
        existingBook.setAuthorName(author.getName());

        books.put(id, existingBook);

        return existingBook;
    }

    // Remove book
    public Boolean removeBook(Long id) {
        Book removed = books.remove(id);
        return removed != null;
    }

    private void validateBookInput(Book book) {
        if (book.getTitle() == null || book.getTitle().trim().isEmpty()) {
            throw new InvalidInputException("Book title must not be empty.");
        }

        if (book.getTitle().length() > 200) {
            throw new InvalidInputException("Book title must not exceed 200 characters.");
        }

        if (book.getIsbn() == null || book.getIsbn().trim().isEmpty()) {
            throw new InvalidInputException("ISBN must not be empty.");
        }

        if (book.getPrice() <= 0) {
            throw new InvalidInputException("Price must be greater than zero.");
        }

        if (book.getPublicationYear() < 1400 || book.getPublicationYear() > 2025) {
            throw new InvalidInputException("Publication year is out of valid range.");
        }

        if (book.getStockQuantity() < 0) {
            throw new InvalidInputException("Stock quantity must not be negative.");
        }

        if (book.getAuthorId() == null || book.getAuthorId() <= 0) {            
            throw new InvalidInputException("Invalid or missing author ID.");
        }
    }

}
