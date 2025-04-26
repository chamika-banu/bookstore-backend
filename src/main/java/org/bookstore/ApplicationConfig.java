package org.bookstore;

import org.bookstore.exception.CustomExceptionMapper;
import org.bookstore.resource.*;
import org.glassfish.jersey.server.ResourceConfig;
import jakarta.ws.rs.ApplicationPath;

@ApplicationPath("/api")  // This sets the base path for the API
public class ApplicationConfig extends ResourceConfig {

    public ApplicationConfig() {
        // Register resource classes
        register(AuthorResource.class);
        register(BookResource.class);
        register(CustomerResource.class);
        register(CartResource.class);
        register(OrderResource.class);
        register(CustomExceptionMapper.class);
    }
}
