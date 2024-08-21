package com.ytech.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author Bruno Pinto
 * @since 21/08/2024
 */
@Entity
@Table(name = "order_movements")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderMovementEntity implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "order_id", nullable = false)
  private Long orderId;

  @Column(name = "stockmovement_id")
  private Long stockMovementId;

  @Column(name = "quantity_used", nullable = false)
  private int quantityUsed;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getOrderId() {
    return orderId;
  }

  public void setOrderId(Long orderId) {
    this.orderId = orderId;
  }

  public Long getStockMovementId() {
    return stockMovementId;
  }

  public void setStockMovementId(Long stockMovementId) {
    this.stockMovementId = stockMovementId;
  }

  public int getQuantityUsed() {
    return quantityUsed;
  }

  public void setQuantityUsed(int quantityUsed) {
    this.quantityUsed = quantityUsed;
  }
}