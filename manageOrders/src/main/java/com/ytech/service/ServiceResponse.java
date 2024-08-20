package com.ytech.service;

import javax.ws.rs.core.Response;

/**
 * @author Bruno Pinto
 * @since 20/08/2024
 */
public class ServiceResponse<T> {
  private T data;
  private Response.Status status;

  public ServiceResponse(T data, Response.Status status) {
      this.data = data;
      this.status = status;
  }

  public T getData() {
    return data;
  }

  public Response.Status getStatus() {
    return status;
  }
}
