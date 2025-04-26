package org.bookstore.resource;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.bookstore.model.Customer;
import org.bookstore.response.ErrorResponse;
import org.bookstore.response.MessageResponse;
import org.bookstore.service.CustomerService;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Path("/customers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CustomerResource {
    private final CustomerService customerService = CustomerService.getInstance();

    @GET
    public Response getAllCustomer() {
        List<Customer> customers = customerService.getAllCustomers();

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("customers", customers);

        return Response.ok(response).build();
    }

    @POST
    public Response createCustomer(Customer customer) {
        Customer createdCustomer = customerService.createCustomer(customer);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("message", "Customer created successfully");
        response.put("customer", createdCustomer);

        return Response.status(Response.Status.CREATED)
                .entity(response)
                .build();
    }

    @GET
    @Path("/{id}")
    public Response getCustomerById(@PathParam("id") Long id) {
        Customer customer = customerService.getCustomerById(id);
        return Response.ok(customer).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateCustomer(@PathParam("id") Long id, Customer customer) {
        Customer updatedCustomer = customerService.updateCustomer(id, customer);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("message", "Customer updated successfully");
        response.put("customer", updatedCustomer);

        return Response.ok(response).build();
    }


    @DELETE
    @Path("/{id}")
    public Response removeCustomer(@PathParam("id") Long id) {
        if (customerService.removeCustomer(id)) {
            MessageResponse message = new MessageResponse("Customer with the ID " + id + " removed successfully");
            return Response.ok(message).build();
        } else {
            ErrorResponse error = new ErrorResponse(404, "No customer found with the given ID.");
            return Response.status(Response.Status.NOT_FOUND).entity(error).build();
        }
    }

}


