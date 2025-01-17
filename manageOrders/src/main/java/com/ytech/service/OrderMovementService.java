package com.ytech.service;

import com.ytech.model.OrderMovementEntity;
import com.ytech.repository.OrderMovementRepository;
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
public class OrderMovementService {

  private final OrderMovementRepository orderMovementRepository;
  private final SessionFactory sessionFactory;

  public OrderMovementService(OrderMovementRepository orderMovementRepository, SessionFactory sessionFactory) {
    this.orderMovementRepository = orderMovementRepository;
    this.sessionFactory = sessionFactory;
  }

  public ServiceResponse<List<OrderMovementEntity>> findAll() {
    Transaction transaction = null;
    try (Session session = sessionFactory.openSession()) {
      transaction = session.beginTransaction();
      List<OrderMovementEntity> orderMovements = orderMovementRepository.findAll(session);
      if (orderMovements.isEmpty()) {
        return new ServiceResponse<>(new ArrayList<>(), Response.Status.NOT_FOUND);
      }
      return new ServiceResponse<>(orderMovements, Response.Status.OK);
    } catch (Exception e) {
      if (transaction != null) {
        transaction.rollback();
      }
      return new ServiceResponse<>(Response.Status.INTERNAL_SERVER_ERROR);
    }
  }

  public ServiceResponse<OrderMovementEntity> findById(Long id) {
    Transaction transaction = null;
    try (Session session = sessionFactory.openSession()) {
      transaction = session.beginTransaction();
      OrderMovementEntity orderMovement = orderMovementRepository.findById(session, id);
      if (orderMovement == null) {
        return new ServiceResponse<>(new OrderMovementEntity(), Response.Status.NOT_FOUND);
      }
      return new ServiceResponse<>(orderMovement, Response.Status.OK);
    } catch (Exception e) {
      if (transaction != null) {
        transaction.rollback();
      }
      return new ServiceResponse<>(Response.Status.INTERNAL_SERVER_ERROR);
    }
  }

  public void save(Session session, OrderMovementEntity orderMovement) {
      orderMovementRepository.save(session, orderMovement);
  }

}
