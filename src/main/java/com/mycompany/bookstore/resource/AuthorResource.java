package com.mycompany.bookstore.resource;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import com.mycompany.bookstore.model.Author;
import com.mycompany.bookstore.model.Book;
import com.mycompany.bookstore.response.MessageResponse;
import com.mycompany.bookstore.service.AuthorService;

import javax.ws.rs.core.MediaType;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


@Path("/authors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthorResource {
    private final AuthorService authorService = AuthorService.getInstance();

    @GET
    public Response getAllAuthors() {
        List<Author> authors = authorService.getAllAuthors();

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("authors", authors);

        return Response.ok(response).build();
    }

    @POST
    public Response createAuthor(Author author) {
        Author createdAuthor = authorService.createAuthor(author);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("message", "Author created successfully");
        response.put("author", createdAuthor);

        return Response.status(Response.Status.CREATED)
                .entity(response)
                .build();
    }

    @GET
    @Path("/{id}")
    public Response getAuthorById(@PathParam("id") Long id) {
        Author author = authorService.getAuthorById(id);
        return Response.ok(author).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateAuthor(@PathParam("id") Long id, Author author) {
        Author updatedAuthor = authorService.updateAuthor(id, author);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("message", "Author updated successfully");
        response.put("author", updatedAuthor);

        return Response.ok(response).build();
    }


    @DELETE
    @Path("/{id}")
    public Response removeAuthor(@PathParam("id") Long id) {
        if (authorService.removeAuthor(id)) {
            MessageResponse message = new MessageResponse("Author with the ID " + id + " removed successfully");
            return Response.ok(message).build();
        } else {
            MessageResponse message = new MessageResponse("No author found with the given ID.");
            return Response.status(Response.Status.NOT_FOUND).entity(message).build();
        }
    }

    @GET
    @Path("/{id}/books")
    public List<Book> getBooksByAuthor(@PathParam("id") Long id) {
        return authorService.getBooksByAuthor(id);
    }

}
