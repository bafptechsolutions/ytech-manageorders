package com.ytech.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.ytech.model.ItemEntity;
import com.ytech.model.UserEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Bruno Pinto
 * @since 22/08/2024
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class TraceOrderDto {
  public TraceOrderDto() {
  }

  private Long id;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
  private LocalDateTime creationDate;

  private int quantity;

  private String status;

  private UserEntity user;

  private ItemEntity item;

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

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public UserEntity getUser() {
    return user;
  }

  public void setUser(UserEntity user) {
    this.user = user;
  }

  public ItemEntity getItem() {
    return item;
  }

  public void setItem(ItemEntity item) {
    this.item = item;
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
