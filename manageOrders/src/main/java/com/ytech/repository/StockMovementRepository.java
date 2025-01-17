package com.ytech.repository;

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
        "SELECT COALESCE(SUM(remainingQuantity), 0) FROM StockMovementEntity WHERE itemId = :itemId", Long.class);
    query.setParameter("itemId", itemId);
    return query.uniqueResult();
  }

  public List<StockMovementEntity> allExistingStocksByItemId(Session session, Long itemId) {
    List<StockMovementEntity> existengStocks = session.createQuery(
            "FROM StockMovementEntity WHERE item_id = :itemId AND remainingQuantity > 0 ORDER BY remainingQuantity ASC", StockMovementEntity.class)
        .setParameter("itemId", itemId)
        .list();
    return existengStocks;
  }

  public Long existsStockMovementItem(Session session, Long itemId) {
    return session.createQuery(
            "SELECT COUNT(id) FROM StockMovementEntity WHERE item_id = :itemId", Long.class)
        .setParameter("itemId", itemId)
        .uniqueResult();
  }

  public List<StockMovementEntity> getAllByArrayIds(Session session, List<Long> ids) {
    return session.createQuery(
            "FROM StockMovementEntity WHERE id IN :ids", StockMovementEntity.class)
        .setParameter("ids", ids)
        .list();
  }
}
