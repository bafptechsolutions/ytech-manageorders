package com.ytech.repository;

import com.ytech.model.ItemEntity;
import org.hibernate.Session;

import java.util.List;

/**
 * @author Bruno Pinto
 * @since 19/08/2024
 */
public class ItemRepository {

  public List<ItemEntity> buscarTodos(Session session) {
    return session.createQuery("FROM ItemEntity", ItemEntity.class).getResultList();
  }
}
