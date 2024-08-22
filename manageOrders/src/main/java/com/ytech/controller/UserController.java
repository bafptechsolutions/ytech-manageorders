package com.ytech.controller;

import com.ytech.dto.OrderDto;
import com.ytech.model.OrderEntity;
import com.ytech.service.LoggerService;
import com.ytech.model.UserEntity;
import com.ytech.service.ServiceResponse;
import com.ytech.service.UserService;
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
@Path("/users")
@Service
public class UserController {

  private final UserService userService;
  private final LoggerService loggerService;

  @Context
  private HttpServletRequest httpRequest;

  @Inject
  public UserController(UserService userService, LoggerService loggerService) {
    this.userService = userService;
    this.loggerService = loggerService;
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response users() {
    ServiceResponse<List<UserEntity>> response = userService.findAll();
    loggerService.logResponse(httpRequest, response.getStatus(), response);
    return Response.status(response.getStatus()).entity(response.getBody()).build();
  }

  @GET
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getUserById(@PathParam("id") Long id) {
    ServiceResponse<UserEntity> response = userService.findById(id);
    loggerService.logResponse(httpRequest, response.getStatus(), response);
    return Response.status(response.getStatus()).entity(response.getBody()).build();
  }

  @GET
  @Path("/{id}/orders")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getAllUserOrdersById(@PathParam("id") Long id) {
    ServiceResponse<List<OrderDto>> response = userService.findAllOrdersById(id);
    loggerService.logResponse(httpRequest, response.getStatus(), response);
    return Response.status(response.getStatus()).entity(response.getBody()).build();
  }

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response createUser(@Valid UserEntity user) {
    ServiceResponse<UserEntity> response = userService.create(user);
    loggerService.logResponse(httpRequest, response.getStatus(), response);
    return Response.status(response.getStatus()).entity(response.getBody()).build();
  }

  @PUT
  @Path("/{id}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response updateUser(@PathParam("id") Long id, UserEntity user) {
    ServiceResponse<UserEntity> response = userService.update(id, user);
    loggerService.logResponse(httpRequest, response.getStatus(), response);
    return Response.status(response.getStatus()).entity(response.getBody()).build();
  }

  @DELETE
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response deleteUser(@PathParam("id") Long id) {
    ServiceResponse<Void> response = userService.delete(id);
    loggerService.logResponse(httpRequest, response.getStatus(), response);
    return Response.status(response.getStatus()).build();
  }
}
