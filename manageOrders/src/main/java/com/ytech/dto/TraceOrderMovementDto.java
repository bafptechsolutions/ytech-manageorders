package com.ytech.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.ytech.model.StockMovementEntity;

/**
 * @author Bruno Pinto
 * @since 22/08/2024
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TraceOrderMovementDto {
  public TraceOrderMovementDto() {
  }

  private Long id;

  private int quantityUsed;

  private StockMovementEntity stockMovement;

  private TraceOrderDto traceOrder;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public int getQuantityUsed() {
    return quantityUsed;
  }

  public void setQuantityUsed(int quantityUsed) {
    this.quantityUsed = quantityUsed;
  }

  public StockMovementEntity getStockMovement() {
    return stockMovement;
  }

  public void setStockMovement(StockMovementEntity stockMovement) {
    this.stockMovement = stockMovement;
  }

  public TraceOrderDto getTraceOrder() {
    return traceOrder;
  }

  public void setTraceOrder(TraceOrderDto traceOrder) {
    this.traceOrder = traceOrder;
  }

}
