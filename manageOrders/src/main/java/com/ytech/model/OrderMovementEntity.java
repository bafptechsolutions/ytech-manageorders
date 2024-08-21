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
@Table(name = "orderMovements")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderMovementEntity implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private int quantityUsed;

//  @ManyToOne
//  @JoinColumn(name = "order_id", nullable = false)
//  private OrderEntity orderEntity;
//
//  @ManyToOne
//  @JoinColumn(name = "stock_movement_id", nullable = false)
//  private StockMovementEntity stockMovementEntity;

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
}