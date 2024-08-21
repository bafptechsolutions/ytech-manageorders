package com.ytech.exception;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Mapeador para as exceções de violação de restrições para os campos nas entity
 * @author Bruno Pinto
 * @since 21/08/2024
 */
@Provider
public class ConstraintViolationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {

  @Override
  public Response toResponse(ConstraintViolationException exception) {
    Set<ConstraintViolation<?>> violations = exception.getConstraintViolations();

    Map<String, String> errors = violations.stream()
        .collect(Collectors.toMap(
            violation -> getSimplePropertyPath(violation.getPropertyPath().toString()),
            ConstraintViolation::getMessage,
            (existing, replacement) -> existing)); // Handle duplicate keys by keeping the first one

    Map<String, Object> responseBody = new HashMap<>();
    responseBody.put("message", "Validation errors");
    responseBody.put("errors", errors);

    return Response.status(Response.Status.BAD_REQUEST)
        .entity(responseBody)
        .build();
  }

  /**
   * Divide o caminho por '.' e devolve o último segmento, que é o nome do campo atual
   * @param fullPath
   * @return
   */
  private String getSimplePropertyPath(String fullPath) {
    String[] parts = fullPath.split("\\.");
    return parts[parts.length - 1];
  }
}
