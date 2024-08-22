package com.ytech.controller;

import com.ytech.service.LoggerService;
import com.ytech.model.OrderMovementEntity;
import com.ytech.service.ServiceResponse;
import com.ytech.service.OrderMovementService;
import org.jvnet.hk2.annotations.Service;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * @author Bruno Pinto
 * @since 21/08/2024
 */
@Path("/orders/movements")
@Service
public class OrderMovementController {

  private final OrderMovementService orderMovementService;
  private final LoggerService loggerService;

  @Inject
  public OrderMovementController(OrderMovementService orderMovementService, LoggerService loggerService) {
    this.orderMovementService = orderMovementService;
    this.loggerService = loggerService;
  }

  @Context
  private HttpServletRequest httpRequest;

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response orderMovements() {
    ServiceResponse<List<OrderMovementEntity>> response = orderMovementService.findAll();
    loggerService.logResponse(httpRequest, response.getStatus(), response);
    return Response.status(response.getStatus()).entity(response.getBody()).build();
  }

  @GET
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getOrderMovementById(@PathParam("id") Long id) {
    ServiceResponse<OrderMovementEntity> response = orderMovementService.findById(id);
    loggerService.logResponse(httpRequest, response.getStatus(), response);
    return Response.status(response.getStatus()).entity(response.getBody()).build();
  }

}
