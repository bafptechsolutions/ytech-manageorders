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

  public OrderMovementEntity findByOrderId(Session session, Long id) {
    return null;
  }

  public List<OrderMovementEntity> getAllByOrderId(Session session, Long orderId) {
    return session.createQuery(
            "FROM OrderMovementEntity WHERE order_id = :orderId", OrderMovementEntity.class)
        .setParameter("orderId", orderId)
        .list();
  }

  public List<OrderMovementEntity> getAllByStockMovementId(Session session, Long stockMovementId) {
    return session.createQuery(
            "FROM OrderMovementEntity WHERE stockMovement_id = :stockMovementId", OrderMovementEntity.class)
        .setParameter("stockMovementId", stockMovementId)
        .list();
  }
}
