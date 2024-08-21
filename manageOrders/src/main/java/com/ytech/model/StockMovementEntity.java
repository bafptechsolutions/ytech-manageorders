package com.ytech.model;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author Bruno Pinto
 * @since 21/08/2024
 */
@Entity
@Table(name = "stockMovements")
public class StockMovementEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
  private LocalDateTime creationDate;

  @ManyToOne
  @JoinColumn(name = "item_id", nullable = false)
  private ItemEntity item;

  @Column(nullable = false)
  private int quantity;

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

  public ItemEntity getItem() {
    return item;
  }

  public void setItem(ItemEntity item) {
    this.item = item;
  }

  public int getQuantity() {
    return quantity;
  }

  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }
}