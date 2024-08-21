package com.ytech.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Bruno Pinto
 * @since 21/08/2024
 */
@Entity
@Table(name = "orders")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderEntity implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
  private LocalDateTime creationDate;

  @Column(nullable = false)
  private int quantity;

  @Column(length = 20, nullable = false, columnDefinition = "VARCHAR(20) DEFAULT 'Pending'")
  private String status;

//  @ManyToOne
//  @JoinColumn(name = "item_id", nullable = false)
//  private ItemEntity itemEntity;

//  @ManyToOne
//  @JoinColumn(name = "userid", nullable = false)
//  private UserEntity userEntity;

//  @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
//  private Set<OrderMovementEntity> orderMovements;


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

//  public UserEntity getUserEntity() {
//    return userEntity;
//  }
//
//  public void setUserEntity(UserEntity userEntity) {
//    this.userEntity = userEntity;
//  }
}
