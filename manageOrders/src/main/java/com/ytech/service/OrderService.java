package com.ytech.service;

import com.ytech.model.ItemEntity;
import com.ytech.model.OrderEntity;
import com.ytech.model.UserEntity;
import com.ytech.repository.OrderRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.jvnet.hk2.annotations.Service;

import javax.ws.rs.core.Response;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * @author Bruno Pinto
 * @since 21/08/2024
 */
@Service
public class OrderService {

  private final OrderRepository orderRepository;
  private final SessionFactory sessionFactory;
  private final StockMovementService stockMovementService;
  private final ItemService itemService;
  private final UserService userService;
  private final EmailService emailService;

  public OrderService(OrderRepository orderRepository, SessionFactory sessionFactory, StockMovementService stockMovementService, ItemService itemService, UserService userService, EmailService emailService) {
    this.orderRepository = orderRepository;
    this.sessionFactory = sessionFactory;
    this.stockMovementService = stockMovementService;
    this.itemService = itemService;
    this.userService = userService;
    this.emailService = emailService;
  }

  public ServiceResponse<List<OrderEntity>> findAll() {
    try (Session session = sessionFactory.openSession()) {
      List<OrderEntity> orders = orderRepository.findAll(session);
      if (orders.isEmpty()) {
        return new ServiceResponse<>(new ArrayList<>(), Response.Status.NOT_FOUND);
      }
      return new ServiceResponse<>(orders, Response.Status.OK);
    } catch (Exception e) {
      return new ServiceResponse<>(Response.Status.INTERNAL_SERVER_ERROR);
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
      return new ServiceResponse<>(Response.Status.INTERNAL_SERVER_ERROR);
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
      OrderEntity order = orderRepository.findById(session, id);
      if (order == null) {
        return new ServiceResponse<>(Response.Status.NOT_FOUND);
      }
      ItemEntity item = session.get(ItemEntity.class, order.getItemId());
      itemService.updateStockQuantity(session, item, item.getQuantityInStock() + order.getQuantity());
      orderRepository.delete(session, order);
      transaction.commit();
      return new ServiceResponse<>(Response.Status.NO_CONTENT);
    } catch (Exception e) {
      if (transaction != null) {
        transaction.rollback();
      }
      return new ServiceResponse<>(Response.Status.INTERNAL_SERVER_ERROR);
    }
  }

  public ServiceResponse<OrderEntity> createOrder(OrderEntity order) {
    Transaction transaction = null;
    try (Session session = sessionFactory.openSession()) {
      transaction = session.beginTransaction();

      UserEntity user = userService.findById(session, order.getUserId());
      if (user == null) {
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("userId", "unregistered user");
        return new ServiceResponse<>(responseBody, Response.Status.BAD_REQUEST);
      }

      ItemEntity item = session.get(ItemEntity.class, order.getItemId());
      if (item == null) {
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("itemId", "does not exist");
        return new ServiceResponse<>(responseBody, Response.Status.NOT_FOUND);
      }

      order.setCreationDate(LocalDateTime.now());
      if (!itemService.hasSufficientStock(session, item.getId(), order.getQuantity()) || !stockMovementService.hasSufficientStock(session, item.getId(), order.getQuantity())) {
        order.setStatus("Pending");
        orderRepository.save(session, order);
        transaction.commit();
        return new ServiceResponse<>("There isn't enough stock available. It will remain in pending status until stock is available.",Response.Status.ACCEPTED);
      }
      itemService.updateStockQuantity(session, item, item.getQuantityInStock() - order.getQuantity());
      order.setStatus("satisfied");
      orderRepository.save(session, order);
      transaction.commit();

      CompletableFuture.runAsync(() -> {
        emailService.sendOrderInformationToUser(order, user);
      });
      return new ServiceResponse<>(order, Response.Status.OK);
    } catch (Exception e) {
      System.out.println(e);
      if (transaction != null) {
        transaction.rollback();
      }
      return new ServiceResponse<>(Response.Status.INTERNAL_SERVER_ERROR);
    }
  }
}