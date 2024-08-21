package com.ytech.service;

import javax.ws.rs.core.Response;

/**
 * @author Bruno Pinto
 * @since 20/08/2024
 */
public class ServiceResponse<T> {
  private final T body;
  private final Response.Status status;

  public ServiceResponse(T body, Response.Status status) {
      this.body = body;
      this.status = status;
  }

  public T getBody() {
    return body;
  }

  public Response.Status getStatus() {
    return status;
  }
}
