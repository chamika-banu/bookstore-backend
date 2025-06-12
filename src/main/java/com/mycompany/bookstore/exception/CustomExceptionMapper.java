package com.mycompany.bookstore.exception;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import com.mycompany.bookstore.response.ErrorResponse;

import java.util.logging.Logger;
import java.util.logging.Level;

@Provider
public class CustomExceptionMapper implements ExceptionMapper<Exception> {

    private static final Logger logger = Logger.getLogger(CustomExceptionMapper.class.getName());

    @Override
    @Produces(MediaType.APPLICATION_JSON)
    public Response toResponse(Exception exception) {

        // Log the exception
        logger.log(Level.SEVERE, "An exception occurred: ", exception);

        if (exception instanceof DuplicateAuthorException) {
            return Response.status(Response.Status.CONFLICT)
                    .entity(new ErrorResponse(409, exception.getMessage()))
                    .build();
        }

        if(exception instanceof AuthorNotFoundException) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ErrorResponse(404, exception.getMessage()))
                    .build();
        }

        if (exception instanceof DuplicateBookException) {
            return Response.status(Response.Status.CONFLICT)
                    .entity(new ErrorResponse(409, exception.getMessage()))
                    .build();
        }

        if(exception instanceof BookNotFoundException) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ErrorResponse(404, exception.getMessage()))
                    .build();
        }

        if (exception instanceof DuplicateCustomerException) {
            return Response.status(Response.Status.CONFLICT)
                    .entity(new ErrorResponse(409, exception.getMessage()))
                    .build();
        }

        if(exception instanceof CustomerNotFoundException) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ErrorResponse(404, exception.getMessage()))
                    .build();
        }

        if(exception instanceof CartNotFoundException) {
            return Response.status(Response.Status.CONFLICT)
                    .entity(new ErrorResponse(409, exception.getMessage()))
                    .build();
        }

        if(exception instanceof BookNotFoundInCartException) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ErrorResponse(404, exception.getMessage()))
                    .build();
        }

        if(exception instanceof OutOfStockException) {
            return Response.status(Response.Status.CONFLICT)
                    .entity(new ErrorResponse(409, exception.getMessage()))
                    .build();
        }

        if(exception instanceof InvalidInputException) {            
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse(400, exception.getMessage()))
                    .build();
        }

        // Fallback error
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ErrorResponse(500, "An unexpected error occurred. Please try again later."))
                .build();
    }
}
