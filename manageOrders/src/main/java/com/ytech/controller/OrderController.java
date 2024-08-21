package com.ytech.controller;

import com.ytech.model.OrderEntity;
import com.ytech.service.ServiceResponse;
import com.ytech.service.OrderService;
import org.jvnet.hk2.annotations.Service;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * @author Bruno Pinto
 * @since 21/08/2024
 */
@Path("/orders")
@Service
public class OrderController {

  private final OrderService orderService;

  @Inject
  public OrderController(OrderService orderService) {
    this.orderService = orderService;
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response orders() {
    ServiceResponse<List<OrderEntity>> response = orderService.findAll();
    return Response.status(response.getStatus()).entity(response.getBody()).build();
  }

  @GET
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getOrderById(@PathParam("id") Long id) {
    ServiceResponse<OrderEntity> response = orderService.findById(id);
    return Response.status(response.getStatus()).entity(response.getBody()).build();
  }

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response createOrder(OrderEntity order) {
    ServiceResponse<OrderEntity> response = orderService.create(order);
    return Response.status(response.getStatus()).entity(response.getBody()).build();
  }

  @PUT
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response cancelOrder(@PathParam("id") Long id) {
    ServiceResponse<OrderEntity> response = orderService.updateStatus(id, "cancelled");
    return Response.status(response.getStatus()).entity(response.getBody()).build();
  }

  @DELETE
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response deleteOrder(@PathParam("id") Long id) {
    ServiceResponse<Void> response = orderService.delete(id);
    return Response.status(response.getStatus()).build();
  }
}
