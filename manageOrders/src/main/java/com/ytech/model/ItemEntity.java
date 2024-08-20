package com.ytech.model;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author Bruno Pinto
 * @since 19/08/2024
 */
@Entity
@Table(name = "items")
public class ItemEntity implements Serializable {

  public ItemEntity() {
  }

  public ItemEntity(String name) {
    this.name = name;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length = 100)
  private String name;

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

}
