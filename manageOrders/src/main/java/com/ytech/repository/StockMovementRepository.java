package com.ytech.repository;

import com.ytech.model.OrderEntity;
import com.ytech.model.StockMovementEntity;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

/**
 * @author Bruno Pinto
 * @since 21/08/2024
 */
public class StockMovementRepository {

  public List<StockMovementEntity> findAll(Session session) {
    return session.createQuery("FROM StockMovementEntity", StockMovementEntity.class).getResultList();
  }

  public List<StockMovementEntity> findAllByItemId(Session session, Long itemId) {
    Query<StockMovementEntity> query = session.createQuery("FROM StockMovementEntity WHERE item_id = :itemId", StockMovementEntity.class);
    query.setParameter("itemId", itemId);
    return query.getResultList();
  }

  public StockMovementEntity findById(Session session, Long id) {
    return session.get(StockMovementEntity.class, id);
  }

  public void save(Session session, StockMovementEntity stockMovementEntity) {
    session.saveOrUpdate(stockMovementEntity);
  }

  public long getCurrentStockForItem(Session session, Long itemId) {
    Query<Long> query = session.createQuery(
        "SELECT COALESCE(SUM(quantity), 0) FROM StockMovementEntity WHERE itemId = :itemId", Long.class);
    query.setParameter("itemId", itemId);
    return query.uniqueResult();
  }
}
