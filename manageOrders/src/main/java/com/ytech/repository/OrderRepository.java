package com.ytech.repository;

import com.ytech.model.OrderEntity;
import org.hibernate.Session;

import java.util.List;

/**
 * @author Bruno Pinto
 * @since 21/08/2024
 */
public class OrderRepository {

  public List<OrderEntity> findAll(Session session) {
    return session.createQuery("FROM OrderEntity", OrderEntity.class).getResultList();
  }

  public OrderEntity findById(Session session, Long id) {
    return session.get(OrderEntity.class, id);
  }

  public void save(Session session, OrderEntity orderEntity) {
    session.saveOrUpdate(orderEntity);
  }

  public void delete(Session session, OrderEntity orderEntity) {
    session.delete(orderEntity);
  }

  public List<OrderEntity> allPendingByItemId(Session session, Long itemId)  {
    List<OrderEntity> existengStocks = session.createQuery(
            "FROM OrderEntity WHERE item_id = :itemId AND status = 'Pending' ORDER BY id ASC", OrderEntity.class)
        .setParameter("itemId", itemId)
        .list();
    return existengStocks;
  }
}
