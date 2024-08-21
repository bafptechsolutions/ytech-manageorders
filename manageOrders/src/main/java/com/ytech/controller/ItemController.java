package com.ytech.controller;

import com.ytech.model.ItemEntity;
import com.ytech.service.ItemService;
import com.ytech.service.ServiceResponse;
import org.jvnet.hk2.annotations.Service;

import javax.inject.Inject;
import javax.ws.rs.*;
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

  @Inject
  public ItemController(ItemService itemService) {
    this.itemService = itemService;
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response items() {
    ServiceResponse<List<ItemEntity>> response = itemService.findAll();
    return Response.status(response.getStatus()).entity(response.getBody()).build();
  }

  @GET
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getItemById(@PathParam("id") Long id) {
    ServiceResponse<ItemEntity> response = itemService.findById(id);
    return Response.status(response.getStatus()).entity(response.getBody()).build();
  }

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response createItem(ItemEntity item) {
    ServiceResponse<ItemEntity> response = itemService.create(item);
    return Response.status(response.getStatus()).entity(response.getBody()).build();
  }

  @PUT
  @Path("/{id}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response updateItem(@PathParam("id") Long id, ItemEntity item) {
    ServiceResponse<ItemEntity> response = itemService.update(id, item);
    return Response.status(response.getStatus()).entity(response.getBody()).build();
  }

  @DELETE
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response deleteItem(@PathParam("id") Long id) {
    ServiceResponse<Void> response = itemService.delete(id);
    return Response.status(response.getStatus()).build();
  }
}
