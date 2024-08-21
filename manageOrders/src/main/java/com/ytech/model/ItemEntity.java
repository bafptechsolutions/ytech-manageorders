package com.ytech.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author Bruno Pinto
 * @since 19/08/2024
 */
@Entity
@Table(name = "items")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ItemEntity implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull(message = "Name is required")
  @Column(nullable = false, length = 100)
  private String name;

//  @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true)
//  private Set<StockMovementEntity> stockMovementEntities;

//  @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true)
//  private Set<OrderEntity> orderEntities;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

//  public Set<StockMovementEntity> getStockMovementEntities() {
//    return stockMovementEntities;
//  }
//
//  public void setStockMovementEntities(Set<StockMovementEntity> stockMovementEntities) {
//    this.stockMovementEntities = stockMovementEntities;
//  }
//
//  public Set<OrderEntity> getOrderEntities() {
//    return orderEntities;
//  }
//
//  public void setOrderEntities(Set<OrderEntity> orderEntities) {
//    this.orderEntities = orderEntities;
//  }
}
