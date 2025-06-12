package com.mycompany.bookstore.resource;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import com.mycompany.bookstore.model.Order;
import com.mycompany.bookstore.response.ErrorResponse;
import com.mycompany.bookstore.service.OrderService;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Path("/customers/{customerId}/orders")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OrderResource {
    private final OrderService orderService = OrderService.getInstance();

    @POST
    public Response createOrder(@PathParam("customerId") Long customerId) {
        Order order = orderService.createOrder(customerId);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Order placed successfully.");
        response.put("order", order);

        return Response.status(Response.Status.CREATED).entity(response).build();
    }

    @GET
    public Response getOrders(@PathParam("customerId") Long customerId) {
        List<Order> orders = orderService.getOrders(customerId);

        Map<String, Object> response = new LinkedHashMap<>();
        if (orders.isEmpty()) {
            response.put("message", "This customer has not placed any orders yet.");
            return Response.ok(response).build();
        }

        response.put("orders", orders);
        return Response.ok(response).build();
    }

    @GET
    @Path("/{orderId}")
    public Response getOrder(@PathParam("customerId") Long customerId, @PathParam("orderId") Long orderId) {
        Order order = orderService.getOrder(customerId, orderId);

        Map<String, Object> response = new LinkedHashMap<>();

        if (order == null) {
            ErrorResponse error = new ErrorResponse(404, "No order for the given ID was found for customer.");
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(error)
                    .build();
        }

        response.put("order", order);
        return Response.ok(response).build();
    }

}
