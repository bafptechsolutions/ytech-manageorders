package com.ytech.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * @author Bruno Pinto
 * @since 21/08/2024
 */
@Entity
@Table(name = "orders")
public class OrderEntity {

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

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @Column(length = 20, nullable = false, columnDefinition = "VARCHAR(20) DEFAULT 'Pending'")
  private String status;

  @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<OrderMovementEntity> orderMovements;

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

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public Set<OrderMovementEntity> getOrderMovements() {
    return orderMovements;
  }

  public void setOrderMovements(Set<OrderMovementEntity> orderMovements) {
    this.orderMovements = orderMovements;
  }
}
