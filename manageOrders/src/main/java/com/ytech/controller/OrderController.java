package com.ytech.controller;

import com.ytech.dto.OrderDto;
import com.ytech.service.LoggerService;
import com.ytech.dto.TraceOrderDto;
import com.ytech.model.OrderEntity;
import com.ytech.service.OrderService;
import com.ytech.service.ServiceResponse;
import com.ytech.service.TraceService;
import org.jvnet.hk2.annotations.Service;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
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
  private final TraceService traceService;
  private final LoggerService loggerService;

  @Inject
  public OrderController(OrderService orderService, TraceService traceService, LoggerService loggerService) {
    this.orderService = orderService;
    this.traceService = traceService;
    this.loggerService = loggerService;
  }

  @Context
  private HttpServletRequest httpRequest;

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response orders() {
    ServiceResponse<List<OrderDto>> response = orderService.findAll();
    loggerService.logResponse(httpRequest, response.getStatus(), response);
    return Response.status(response.getStatus()).entity(response.getBody()).build();
  }

  @GET
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getOrderById(@PathParam("id") Long id) {
    ServiceResponse<OrderDto> response = orderService.findById(id);
    loggerService.logResponse(httpRequest, response.getStatus(), response);
    return Response.status(response.getStatus()).entity(response.getBody()).build();
  }

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response createOrder(@Valid OrderEntity order) {
    ServiceResponse<OrderEntity> response = orderService.createOrder(order);
    loggerService.logResponse(httpRequest, response.getStatus(), response);
    return Response.status(response.getStatus()).entity(response.getBody()).build();
  }

  @DELETE
  @Path("/{id}/")
  @Produces(MediaType.APPLICATION_JSON)
  public Response deleteOrder(@PathParam("id") Long id) {
    ServiceResponse<Void> response = orderService.delete(id);
    loggerService.logResponse(httpRequest, response.getStatus(), response);
    return Response.status(response.getStatus()).entity(response.getBody()).build();
  }

  @GET
  @Path("/{id}/trace")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getTraceStockMovementById(@PathParam("id") Long id) {
    ServiceResponse<TraceOrderDto> response = traceService.traceOrderMovementById(id);
    loggerService.logResponse(httpRequest, response.getStatus(), response);
    return Response.status(response.getStatus()).entity(response.getBody()).build();
  }
}
