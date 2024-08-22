package com.ytech.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Bruno Pinto
 * @since 21/08/2024
 */
@Entity
@Table(name = "stock_movements")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StockMovementEntity implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
  @Column(name = "creationdate", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
  private LocalDateTime creationDate;

  @NotNull
  @Column(name = "item_id", nullable = false)
  private Long itemId;

  @NotNull(message = "Quantity is required")
  @Min(value = 1, message = "minimum value of 1")
  @Column(nullable = false)
  private int quantity;

  @Column(name = "remaining_quantity", nullable = false)
  private int remainingQuantity;

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

  public @NotNull Long getItemId() {
    return itemId;
  }

  public void setItemId(@NotNull Long itemId) {
    this.itemId = itemId;
  }

  public int getRemainingQuantity() {
    return remainingQuantity;
  }

  public void setRemainingQuantity(int remainingQuantity) {
    this.remainingQuantity = remainingQuantity;
  }
}