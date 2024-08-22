package com.ytech.repository;

import com.ytech.model.ItemEntity;
import org.hibernate.Session;

import java.util.List;

/**
 * @author Bruno Pinto
 * @since 19/08/2024
 */
public class ItemRepository {

  public List<ItemEntity> findAll(Session session) {
    return session.createQuery("FROM ItemEntity", ItemEntity.class).getResultList();
  }

  public ItemEntity findById(Session session, Long id) {
    return session.get(ItemEntity.class, id);
  }

  public void save(Session session, ItemEntity item) {
    session.saveOrUpdate(item);
  }

  public void delete(Session session, ItemEntity item) {
    session.delete(item);
  }

}
