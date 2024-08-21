package com.ytech.controller;

import com.ytech.model.UserEntity;
import com.ytech.service.ServiceResponse;
import com.ytech.service.UserService;
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
@Path("/users")
@Service
public class UserController {

  private final UserService userService;

  @Inject
  public UserController(UserService userService) {
    this.userService = userService;
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response users() {
    ServiceResponse<List<UserEntity>> response = userService.findAll();
    return Response.status(response.getStatus()).entity(response.getBody()).build();
  }

  @GET
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getUserById(@PathParam("id") Long id) {
    ServiceResponse<UserEntity> response = userService.findById(id);
    return Response.status(response.getStatus()).entity(response.getBody()).build();
  }

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response createUser(UserEntity user) {
    ServiceResponse<UserEntity> response = userService.create(user);
    return Response.status(response.getStatus()).entity(response.getBody()).build();
  }

  @PUT
  @Path("/{id}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response updateUser(@PathParam("id") Long id, UserEntity user) {
    ServiceResponse<UserEntity> response = userService.update(id, user);
    return Response.status(response.getStatus()).entity(response.getBody()).build();
  }

  @DELETE
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response deleteUser(@PathParam("id") Long id) {
    ServiceResponse<Void> response = userService.delete(id);
    return Response.status(response.getStatus()).build();
  }
}
