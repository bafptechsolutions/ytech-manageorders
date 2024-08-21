package com.ytech.service;

import com.ytech.model.OrderEntity;
import com.ytech.repository.OrderRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Bruno Pinto
 * @since 21/08/2024
 */
public class OrderService {

  private final OrderRepository orderRepository;
  private final SessionFactory sessionFactory;

  public OrderService(OrderRepository orderRepository, SessionFactory sessionFactory) {
    this.orderRepository = orderRepository;
    this.sessionFactory = sessionFactory;
  }

  public ServiceResponse<List<OrderEntity>> findAll() {
    try (Session session = sessionFactory.openSession()) {
      List<OrderEntity> orders = orderRepository.findAll(session);
      if (orders.isEmpty()) {
        return new ServiceResponse<>(new ArrayList<>(), Response.Status.NOT_FOUND);
      }
      return new ServiceResponse<>(orders, Response.Status.OK);
    } catch (Exception e) {
      return new ServiceResponse<>(null, Response.Status.INTERNAL_SERVER_ERROR);
    }
  }

  public ServiceResponse<OrderEntity> findById(Long id) {
    try (Session session = sessionFactory.openSession()) {
      OrderEntity order = orderRepository.findById(session, id);
      if (order == null) {
        return new ServiceResponse<>(new OrderEntity(), Response.Status.NOT_FOUND);
      }
      return new ServiceResponse<>(order, Response.Status.OK);
    } catch (Exception e) {
      return new ServiceResponse<>(null, Response.Status.INTERNAL_SERVER_ERROR);
    }
  }

  public ServiceResponse<OrderEntity> create(OrderEntity order) {
    Transaction transaction = null;
    try (Session session = sessionFactory.openSession()) {
      transaction = session.beginTransaction();
      orderRepository.save(session, order);
      transaction.commit();
      return new ServiceResponse<>(order, Response.Status.CREATED);
    } catch (Exception e) {
      if (transaction != null) {
        transaction.rollback();
      }
      return new ServiceResponse<>(null, Response.Status.INTERNAL_SERVER_ERROR);
    }
  }

  public ServiceResponse<OrderEntity> updateStatus(Long id, String status) {
    Transaction transaction = null;
    try (Session session = sessionFactory.openSession()) {
      transaction = session.beginTransaction();
      OrderEntity existingOrder = orderRepository.findById(session, id);
      if (existingOrder == null) {
        return new ServiceResponse<>(new OrderEntity(), Response.Status.NOT_FOUND);
      }
      existingOrder.setStatus(status);
      orderRepository.save(session, existingOrder);
      transaction.commit();
      return new ServiceResponse<>(null, Response.Status.NO_CONTENT);
    } catch (Exception e) {
      if (transaction != null) {
        transaction.rollback();
      }
      return new ServiceResponse<>(null, Response.Status.INTERNAL_SERVER_ERROR);
    }
  }

  public ServiceResponse<Void> delete(Long id) {
    Transaction transaction = null;
    try (Session session = sessionFactory.openSession()) {
      transaction = session.beginTransaction();
      OrderEntity order = orderRepository.findById(session, id);
      if (order == null) {
        return new ServiceResponse<>(null, Response.Status.NOT_FOUND);
      }
      orderRepository.delete(session, order);
      transaction.commit();
      return new ServiceResponse<>(null, Response.Status.NO_CONTENT);
    } catch (Exception e) {
      if (transaction != null) {
        transaction.rollback();
      }
      return new ServiceResponse<>(null, Response.Status.INTERNAL_SERVER_ERROR);
    }
  }
}