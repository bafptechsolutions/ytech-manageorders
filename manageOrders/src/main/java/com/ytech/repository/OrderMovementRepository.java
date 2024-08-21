package com.ytech.repository;

import com.ytech.model.OrderMovementEntity;
import org.hibernate.Session;

import java.util.List;

/**
 * @author Bruno Pinto
 * @since 21/08/2024
 */
public class OrderMovementRepository {

  public List<OrderMovementEntity> findAll(Session session) {
    return session.createQuery("FROM OrderMovementEntity", OrderMovementEntity.class).getResultList();
  }

  public OrderMovementEntity findById(Session session, Long id) {
    return session.get(OrderMovementEntity.class, id);
  }

  public void save(Session session, OrderMovementEntity orderMovementEntity) {
    session.saveOrUpdate(orderMovementEntity);
  }

}
