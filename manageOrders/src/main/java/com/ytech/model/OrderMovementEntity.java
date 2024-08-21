package com.ytech.model;

import javax.persistence.*;

/**
 * @author Bruno Pinto
 * @since 21/08/2024
 */
@Entity
@Table(name = "orderMovements")
public class OrderMovementEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "order_id", nullable = false)
  private OrderEntity orderEntity;

  @ManyToOne
  @JoinColumn(name = "stock_movement_id", nullable = false)
  private StockMovementEntity stockMovementEntity;

  @Column(nullable = false)
  private int quantityUsed;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public OrderEntity getOrderEntity() {
    return orderEntity;
  }

  public void setOrderEntity(OrderEntity orderEntity) {
    this.orderEntity = orderEntity;
  }

  public StockMovementEntity getStockMovementEntity() {
    return stockMovementEntity;
  }

  public void setStockMovementEntity(StockMovementEntity stockMovementEntity) {
    this.stockMovementEntity = stockMovementEntity;
  }

  public int getQuantityUsed() {
    return quantityUsed;
  }

  public void setQuantityUsed(int quantityUsed) {
    this.quantityUsed = quantityUsed;
  }
}