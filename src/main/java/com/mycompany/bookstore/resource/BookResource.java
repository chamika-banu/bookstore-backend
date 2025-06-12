package com.mycompany.bookstore.resource;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import com.mycompany.bookstore.model.Book;
import com.mycompany.bookstore.response.MessageResponse;
import com.mycompany.bookstore.response.ErrorResponse;
import com.mycompany.bookstore.service.BookService;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Path("/books")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BookResource {
    private final BookService bookService = BookService.getInstance();

    @GET
    public Response getAllBooks() {
        List<Book> books = bookService.getAllBooks();

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("books", books);

        return Response.ok(response).build();
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
        response.put("book", createdBook);

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
        response.put("book", updatedBook);

        return Response.ok(response).build();
    }

    @DELETE
    @Path("/{id}")
    public Response removeBook(@PathParam("id") Long id) {
        if (bookService.removeBook(id)) {
            MessageResponse message = new MessageResponse("Book with the ID " + id + " removed successfully");
            return Response.ok(message).build();
        } else {
            ErrorResponse error = new ErrorResponse(404, "No book found with the given ID.");
            return Response.status(Response.Status.NOT_FOUND).entity(error).build();
        }
    }

}
