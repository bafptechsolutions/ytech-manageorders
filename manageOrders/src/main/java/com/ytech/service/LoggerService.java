package com.ytech.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.log4j.Logger;
import org.jvnet.hk2.annotations.Service;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;

/**
 * @author Bruno Pinto
 * @since 22/08/2024
 */
@Service
public class LoggerService {

  private static final Logger logger = Logger.getLogger(LoggerService.class);

  public void logResponse(HttpServletRequest request, Response.Status status, Object response) {
    try {
      ObjectMapper mapper = new ObjectMapper();
      mapper.registerModule(new JavaTimeModule());
      mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
      String jsonResponse = mapper.writeValueAsString(response);
      logger.info("Response for request: uri: " + request.getRequestURI() +
          ", response: " + jsonResponse);
    } catch (Exception e) {
      logger.error("Failed to serialize response", e);
    }
  }

  public void logError(HttpServletRequest request, Exception e) {
    logger.error("Error processing request: uri: " + request.getRequestURI() + ", error: " + e);
  }

  public void logError(Exception e) {
    logger.error("Error processing request: error: " + e);
  }
}
