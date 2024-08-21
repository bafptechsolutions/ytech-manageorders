package com.ytech.service;

import com.ytech.model.UserEntity;
import com.ytech.repository.UserRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.jvnet.hk2.annotations.Service;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Bruno Pinto
 * @since 21/08/2024
 */
@Service
public class UserService {

  private final UserRepository userRepository;
  private final SessionFactory sessionFactory;

  public UserService(UserRepository userRepository, SessionFactory sessionFactory) {
    this.userRepository = userRepository;
    this.sessionFactory = sessionFactory;
  }

  public ServiceResponse<List<UserEntity>> findAll() {
    try (Session session = sessionFactory.openSession()) {
      List<UserEntity> users = userRepository.findAll(session);
      if (users.isEmpty()) {
        return new ServiceResponse<>(new ArrayList<>(), Response.Status.NOT_FOUND);
      }
      return new ServiceResponse<>(users, Response.Status.OK);
    } catch (Exception e) {
      return new ServiceResponse<>(Response.Status.INTERNAL_SERVER_ERROR);
    }
  }

  public ServiceResponse<UserEntity> findById(Long id) {
    try (Session session = sessionFactory.openSession()) {
      UserEntity user = userRepository.findById(session, id);
      if (user == null) {
        return new ServiceResponse<>(new UserEntity(), Response.Status.NOT_FOUND);
      }
      return new ServiceResponse<>(user, Response.Status.OK);
    } catch (Exception e) {
      return new ServiceResponse<>(Response.Status.INTERNAL_SERVER_ERROR);
    }
  }

  public ServiceResponse<UserEntity> findAllOrdersById(Long id) {
    try (Session session = sessionFactory.openSession()) {
      UserEntity user = userRepository.findById(session, id);
      if (user == null) {
        return new ServiceResponse<>(new UserEntity(), Response.Status.NOT_FOUND);
      }
      return new ServiceResponse<>(user, Response.Status.OK);
    } catch (Exception e) {
      return new ServiceResponse<>(Response.Status.INTERNAL_SERVER_ERROR);
    }
  }

  public ServiceResponse<UserEntity> create(UserEntity user) {
    Transaction transaction = null;
    try (Session session = sessionFactory.openSession()) {
      transaction = session.beginTransaction();
      userRepository.save(session, user);
      transaction.commit();
      return new ServiceResponse<>(user, Response.Status.CREATED);
    } catch (Exception e) {
      if (transaction != null) {
        transaction.rollback();
      }
      return new ServiceResponse<>(Response.Status.INTERNAL_SERVER_ERROR);
    }
  }

  public ServiceResponse<UserEntity> update(Long id, UserEntity user) {
    Transaction transaction = null;
    try (Session session = sessionFactory.openSession()) {
      transaction = session.beginTransaction();
      UserEntity existingUser = userRepository.findById(session, id);
      if (existingUser == null) {
        return new ServiceResponse<>(new UserEntity(), Response.Status.NOT_FOUND);
      }
      existingUser.setName(user.getName());
      existingUser.setEmail(user.getEmail());
      userRepository.save(session, existingUser);
      transaction.commit();
      return new ServiceResponse<>(Response.Status.NO_CONTENT);
    } catch (Exception e) {
      if (transaction != null) {
        transaction.rollback();
      }
      return new ServiceResponse<>(Response.Status.INTERNAL_SERVER_ERROR);
    }
  }

  public ServiceResponse<Void> delete(Long id) {
    Transaction transaction = null;
    try (Session session = sessionFactory.openSession()) {
      transaction = session.beginTransaction();
      UserEntity user = userRepository.findById(session, id);
      if (user == null) {
        return new ServiceResponse<>(Response.Status.NOT_FOUND);
      }
      userRepository.delete(session, user);
      transaction.commit();
      return new ServiceResponse<>(Response.Status.NO_CONTENT);
    } catch (Exception e) {
      if (transaction != null) {
        transaction.rollback();
      }
      return new ServiceResponse<>(Response.Status.INTERNAL_SERVER_ERROR);
    }
  }

  public boolean userExists(Session session, Long userId) {
    return userRepository.userExists(session, userId);
  }

  public void sendEmail(Long userId, String message) {
    System.out.println("EMAIL --------------------------------------------");
    System.out.println(userId);
    System.out.println(message);
    System.out.println("EMAIL --------------------------------------------");
  }

}
