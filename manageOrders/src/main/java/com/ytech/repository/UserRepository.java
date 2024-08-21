package com.ytech.repository;

import com.ytech.model.UserEntity;
import org.hibernate.Session;

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
}
