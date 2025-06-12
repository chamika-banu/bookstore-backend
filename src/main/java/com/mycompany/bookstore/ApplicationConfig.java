package com.mycompany.bookstore;

import com.mycompany.bookstore.exception.CustomExceptionMapper;
import com.mycompany.bookstore.resource.*;
import org.glassfish.jersey.server.ResourceConfig;
import javax.ws.rs.ApplicationPath;

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
