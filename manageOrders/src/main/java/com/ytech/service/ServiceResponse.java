package com.ytech.service;

import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

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

  public ServiceResponse(Response.Status status) {
    this.body = (T) "";
    this.status = status;
  }

  public ServiceResponse(String message, Response.Status status) {
    Map<String, Object> responseBody = new HashMap<>();
    responseBody.put("message", message);
    this.body = (T) responseBody;
    this.status = status;
  }

  public ServiceResponse(Map<String, Object> errorsBody, Response.Status status) {
    Map<String, Object> responseBody = new HashMap<>();
    responseBody.put("errors", errorsBody);
    this.body = (T) responseBody;
    this.status = status;
  }

  public T getBody() {
    return body;
  }

  public Response.Status getStatus() {
    return status;
  }
}
