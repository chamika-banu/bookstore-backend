package com.mycompany.bookstore.resource;

import com.mycompany.bookstore.response.MessageResponse;
import com.mycompany.bookstore.service.CartService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import com.mycompany.bookstore.model.CartItem;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Path("/customers/{customerId}/cart")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CartResource {
    private final CartService cartService = CartService.getInstance();

    @GET
    public Response getCart(@PathParam("customerId") Long customerId) {
        List<CartItem> cart = cartService.getCart(customerId);

        if (cart == null || cart.isEmpty()) {
            return Response.status(Response.Status.OK)
                    .entity(new MessageResponse("Cart is empty."))
                    .build();
        }

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("customer cart", cart);
        return Response.ok(response).build();

    }

    @POST
    @Path("/items")
    public Response addItem(@PathParam("customerId") Long customerId, CartItem item) {
        List<CartItem> cart = cartService.addItem(customerId, item);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("message", "Item added to cart");
        response.put("customer cart", cart);

        return Response.ok(response).build();
    }

    @PUT
    @Path("/items/{bookId}")
    public Response updateItem(@PathParam("customerId") Long customerId, @PathParam("bookId") Long bookId, CartItem item) {
        List<CartItem> cart = cartService.updateItem(customerId, bookId, item);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("message", "Cart updated");
        response.put("customer cart", cart);

        return Response.ok(response).build();
    }

    @DELETE
    @Path("/items/{bookId}")
    public Response removeItem(@PathParam("customerId") Long customerId, @PathParam("bookId") Long bookId) {
        List<CartItem> cart = cartService.removeItem(customerId, bookId);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("message", "Book with ID " + bookId + " removed from cart");
        response.put("customer cart", cart);

        return Response.ok(response).build();
    }
}
