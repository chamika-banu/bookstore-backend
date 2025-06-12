package com.mycompany.bookstore.service;

import com.mycompany.bookstore.exception.AuthorNotFoundException;
import com.mycompany.bookstore.exception.DuplicateAuthorException;
import com.mycompany.bookstore.exception.InvalidInputException;
import com.mycompany.bookstore.model.Author;
import com.mycompany.bookstore.model.Book;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class AuthorService {
    private static final AtomicLong idCounter = new AtomicLong(0);

    // Collection of authors
    private static final Map<Long, Author> authors = new HashMap<>();

    // Initialize singleton instance
    private static final AuthorService instance = new AuthorService();


    private AuthorService() {
        // Sample data to simulate some authors in memory
        authors.put(idCounter.incrementAndGet(), new Author(idCounter.get(), "J.K. Rowling", "British author"));
        authors.put(idCounter.incrementAndGet(), new Author(idCounter.get(), "George R.R. Martin", "American author"));
    }

    public static AuthorService getInstance() {
        return instance;
    }

    // Create author
    public Author createAuthor(Author author) {
        validateAuthorInput(author);
        // Check if an author with the same name and email already exists in the collection
        for(Author currentAuthor : authors.values()) {
            if(currentAuthor.getName().equalsIgnoreCase(author.getName())) {
                throw new DuplicateAuthorException("Author with name '" + author.getName() + "' already exists.");
            }
        }

        Long id = idCounter.incrementAndGet();
        author.setId(id);
        authors.put(id, author);
        return author;
    }

    // Get all authors
    public List<Author> getAllAuthors() {
        // Convert Map values to a List
        return new ArrayList<>(authors.values());
    }

    // Get author by ID
    public Author getAuthorById(Long id) {
        Author author = authors.get(id);
        if(author == null) {
            throw new AuthorNotFoundException("Author with ID " + id + " not found.");
        }

        return author;
    }

    // Update author
    public Author updateAuthor(Long id, Author updatedAuthor) {
        validateAuthorInput(updatedAuthor);
        Author existingAuthor = authors.get(id);

        if (existingAuthor == null) {
            throw new AuthorNotFoundException("Author with ID " + id + " not found.");
        }

        // Update the existing author's details
        existingAuthor.setName(updatedAuthor.getName());
        existingAuthor.setBiography(updatedAuthor.getBiography());

        authors.put(id, existingAuthor);

        return existingAuthor;
    }

    // Remove author
    public Boolean removeAuthor(Long id) {
        Author removed = authors.remove(id);
        return removed != null;
    }

    // Get books by author
    public List<Book> getBooksByAuthor(Long id) {
        Author author = authors.get(id);

        if(author == null) {
            throw new AuthorNotFoundException("Author with ID " + id + " not found.");
        }

        List<Book> authorBooks = new ArrayList<>();
        for(Book book : BookService.getInstance().getAllBooks()) {
            if(book.getAuthorId().equals(id)) {
                book.setAuthorName(author.getName());
                authorBooks.add(book);
            }
        }

        return authorBooks;
    }

    private void validateAuthorInput(Author author) {
        if (author.getName() == null || author.getName().trim().isEmpty()) {
            throw new InvalidInputException("Author name must not be empty.");
        }

        if (author.getBiography() == null || author.getBiography().trim().isEmpty()) {
            throw new InvalidInputException("Author biography must not be empty.");
        }

        if (author.getName().length() > 100) {
            throw new InvalidInputException("Author name must not exceed 100 characters.");
        }

        if (author.getBiography().length() > 1000) {
            throw new InvalidInputException("Author biography must not exceed 400 characters.");
        }
    }



}
