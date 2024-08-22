package com.ytech.controller;

import com.ytech.service.LoggerService;
import com.ytech.model.ItemEntity;
import com.ytech.service.ItemService;
import com.ytech.service.ServiceResponse;
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
 * @since 19/08/2024
 */
@Path("/items")
@Service
public class ItemController {

  private final ItemService itemService;
  private final LoggerService loggerService;

  @Inject
  public ItemController(ItemService itemService, LoggerService loggerService) {
    this.itemService = itemService;
    this.loggerService = loggerService;
  }

  @Context
  private HttpServletRequest httpRequest;

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response items() {
    ServiceResponse<List<ItemEntity>> response = itemService.findAll();
    loggerService.logResponse(httpRequest, response.getStatus(), response);
    return Response.status(response.getStatus()).entity(response.getBody()).build();
  }

  @GET
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getItemById(@PathParam("id") Long id) {
    ServiceResponse<ItemEntity> response = itemService.findById(id);
    loggerService.logResponse(httpRequest, response.getStatus(), response);
    return Response.status(response.getStatus()).entity(response.getBody()).build();
  }

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response createItem(@Valid ItemEntity item) {
    ServiceResponse<ItemEntity> response = itemService.create(item);
    loggerService.logResponse(httpRequest, response.getStatus(), response);
    return Response.status(response.getStatus()).entity(response.getBody()).build();
  }

  @PUT
  @Path("/{id}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response updateItem(@PathParam("id") Long id, @Valid ItemEntity item) {
    ServiceResponse<ItemEntity> response = itemService.update(id, item);
    loggerService.logResponse(httpRequest, response.getStatus(), response);
    return Response.status(response.getStatus()).entity(response.getBody()).build();
  }

  @DELETE
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response deleteItem(@PathParam("id") Long id) {
    ServiceResponse<Void> response = itemService.delete(id);
    loggerService.logResponse(httpRequest, response.getStatus(), response);
    return Response.status(response.getStatus()).build();
  }
}
