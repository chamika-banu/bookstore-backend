package org.bookstore.resource;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.bookstore.model.Book;
import org.bookstore.response.MessageResponse;
import org.bookstore.service.BookService;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Path("/books")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BookResource {
    private final BookService bookService = BookService.getInstance();

    @GET
    public List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }

    @GET
    @Path("/{id}")
    public Response getBookById(@PathParam("id") Long id) {
        Book book = bookService.getBookById(id);
        return Response.ok(book).build();
    }

    @POST
    public Response createBook(Book book) {
        Book createdBook = bookService.createBook(book);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("message", "Book created successfully");
        response.put("author", createdBook);

        return Response.status(Response.Status.CREATED)
                .entity(response)
                .build();
    }

    @PUT
    @Path("/{id}")
    public Response updateBook(@PathParam("id") Long id, Book book) {
        Book updatedBook = bookService.updateBook(id, book);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("message", "Book updated successfully");
        response.put("author", updatedBook);

        return Response.ok(response).build();
    }

    @DELETE
    @Path("/{id}")
    public Response removeBook(@PathParam("id") Long id) {
        if (bookService.removeBook(id)) {
            MessageResponse message = new MessageResponse("Book with the ID " + id + " removed successfully");
            return Response.ok(message).build();
        } else {
            MessageResponse message = new MessageResponse("No book found with the given ID.");
            return Response.status(Response.Status.NOT_FOUND).entity(message).build();
        }
    }

}
