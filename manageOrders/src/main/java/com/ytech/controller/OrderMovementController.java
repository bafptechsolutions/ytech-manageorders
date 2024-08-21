package com.ytech.controller;

import com.ytech.model.OrderMovementEntity;
import com.ytech.service.ServiceResponse;
import com.ytech.service.OrderMovementService;
import org.jvnet.hk2.annotations.Service;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
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

  @Inject
  public OrderMovementController(OrderMovementService orderMovementService) {
    this.orderMovementService = orderMovementService;
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response orderMovements() {
    ServiceResponse<List<OrderMovementEntity>> response = orderMovementService.findAll();
    return Response.status(response.getStatus()).entity(response.getBody()).build();
  }

  @GET
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getOrderMovementById(@PathParam("id") Long id) {
    ServiceResponse<OrderMovementEntity> response = orderMovementService.findById(id);
    return Response.status(response.getStatus()).entity(response.getBody()).build();
  }

}
