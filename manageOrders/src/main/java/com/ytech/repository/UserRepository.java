package com.ytech.repository;

import com.ytech.model.UserEntity;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

/**
 * @author Bruno Pinto
 * @since 21/08/2024
 */
public class UserRepository {

  public List<UserEntity> findAll(Session session) {
    return session.createQuery("FROM UserEntity", UserEntity.class).getResultList();
  }

  public UserEntity findById(Session session, Long id) {
    return session.get(UserEntity.class, id);
  }

  public void save(Session session, UserEntity userEntity) {
    session.saveOrUpdate(userEntity);
  }

  public void delete(Session session, UserEntity userEntity) {
    session.delete(userEntity);
  }

  public boolean userExists(Session session, Long userId) {
    Query<Long> query = session.createQuery(
        "SELECT COUNT(id) FROM UserEntity WHERE id = :userId", Long.class);
    query.setParameter("userId", userId);
    Long count = query.uniqueResult();
    return count != null && count > 0;
  }
}
