package com.ytech.controller;

import com.ytech.model.OrderEntity;
import com.ytech.model.StockMovementEntity;
import com.ytech.service.ServiceResponse;
import com.ytech.service.StockMovementService;
import org.jvnet.hk2.annotations.Service;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * @author Bruno Pinto
 * @since 21/08/2024
 */
@Path("/stocks/movements")
@Service
public class StockMovementController {

  private final StockMovementService stockMovementService;

  @Inject
  public StockMovementController(StockMovementService stockMovementService) {
    this.stockMovementService = stockMovementService;
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response stockMovements() {
    ServiceResponse<List<StockMovementEntity>> response = stockMovementService.findAll();
    return Response.status(response.getStatus()).entity(response.getBody()).build();
  }

  @GET
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getStockMovementById(@PathParam("id") Long id) {
    ServiceResponse<StockMovementEntity> response = stockMovementService.findById(id);
    return Response.status(response.getStatus()).entity(response.getBody()).build();
  }

  @GET
  @Path("/items/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getStockMovementByItemId(@PathParam("id") Long id) {
    ServiceResponse<List<StockMovementEntity>> response = stockMovementService.findAllByItemId(id);
    return Response.status(response.getStatus()).entity(response.getBody()).build();
  }

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response createOrder(@Valid StockMovementEntity stockMovementEntity) {
    ServiceResponse<StockMovementEntity> response = stockMovementService.createStockMovement(stockMovementEntity);
    return Response.status(response.getStatus()).entity(response.getBody()).build();
  }

}
