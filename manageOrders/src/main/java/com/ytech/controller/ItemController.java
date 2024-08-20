package com.ytech.controller;

import com.ytech.model.ItemEntity;
import com.ytech.service.ItemService;
import com.ytech.service.ServiceResponse;
import org.jvnet.hk2.annotations.Service;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collection;

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
    ServiceResponse<Collection<ItemEntity>> items = itemService.todos();
    return Response.status(items.getStatus()).entity(items.getData()).build();
  }
}
