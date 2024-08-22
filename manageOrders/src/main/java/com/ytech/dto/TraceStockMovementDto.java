package com.ytech.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Bruno Pinto
 * @since 22/08/2024
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TraceStockMovementDto {
  public TraceStockMovementDto() {
  }

  private Long id;
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
  private LocalDateTime creationDate;
  private int quantity;
  private int remainingQuantity;
  private List<TraceOrderMovementDto> orderMovement = new ArrayList<>();

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public LocalDateTime getCreationDate() {
    return creationDate;
  }

  public void setCreationDate(LocalDateTime creationDate) {
    this.creationDate = creationDate;
  }

  public int getQuantity() {
    return quantity;
  }

  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }

  public int getRemainingQuantity() {
    return remainingQuantity;
  }

  public void setRemainingQuantity(int remainingQuantity) {
    this.remainingQuantity = remainingQuantity;
  }

  public List<TraceOrderMovementDto> getOrderMovement() {
    return orderMovement;
  }

  public void setOrderMovement(List<TraceOrderMovementDto> orderMovement) {
    this.orderMovement = orderMovement;
  }

  public void addOrderMovement(TraceOrderMovementDto orderMovement) {
    this.orderMovement.add(orderMovement);
  }
}
