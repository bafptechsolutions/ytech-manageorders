package com.ytech.service;

import com.ytech.model.*;
import com.ytech.repository.OrderRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.jvnet.hk2.annotations.Service;

import javax.ws.rs.core.Response;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
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
  private final OrderMovementService orderMovementService;

  public OrderService(OrderRepository orderRepository, SessionFactory sessionFactory, StockMovementService stockMovementService, ItemService itemService, UserService userService, EmailService emailService, OrderMovementService orderMovementService) {
    this.orderRepository = orderRepository;
    this.sessionFactory = sessionFactory;
    this.stockMovementService = stockMovementService;
    this.itemService = itemService;
    this.userService = userService;
    this.emailService = emailService;
    this.orderMovementService = orderMovementService;
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

  public void processOrder(OrderEntity orderEntity, UserEntity userEntity) {
    Transaction transaction = null;

    try (Session session = sessionFactory.openSession()) {
      transaction = session.beginTransaction();

      if (stockMovementService.hasSufficientStock(session, orderEntity.getItemId(), orderEntity.getQuantity())) {
        List<StockMovementEntity> stockMovements = stockMovementService.allExistingStocksByItemId(session, orderEntity.getItemId());

        int accumulatedQuantity = 0;
        for (StockMovementEntity stockMovementEntity : stockMovements) {
          if (accumulatedQuantity >= orderEntity.getQuantity()) {
            break;
          }

          int availableQuantity = stockMovementEntity.getRemainingQuantity();

          OrderMovementEntity orderMovementEntity = new OrderMovementEntity();
          orderMovementEntity.setOrderId(orderEntity.getId());
          orderMovementEntity.setStockMovementId(stockMovementEntity.getId());

          if (accumulatedQuantity + availableQuantity <= orderEntity.getQuantity()) {
            // utilizar todo do mesmo registo
            accumulatedQuantity += availableQuantity;
            stockMovementEntity.setRemainingQuantity(availableQuantity - orderEntity.getQuantity());
            orderMovementEntity.setQuantityUsed(orderEntity.getQuantity());
          } else {
            // ir utilizando de vários registos
            int needed = orderEntity.getQuantity() - accumulatedQuantity;
            accumulatedQuantity += needed;
            stockMovementEntity.setRemainingQuantity(availableQuantity - needed);
            orderMovementEntity.setQuantityUsed(needed);
          }

          session.update(stockMovementEntity);
          orderMovementService.save(session, orderMovementEntity);
        }

        /*
          Caso durante o processamento concorrente já não exista quantidade suficiente
         */
        if (accumulatedQuantity < orderEntity.getQuantity()) {
          transaction.rollback();
        } else {
          orderEntity.setStatus("satisfied");
          orderRepository.save(session, orderEntity);
          transaction.commit();
        }
      }

      CompletableFuture.runAsync(() -> {
        emailService.sendOrderInformationToUser(orderEntity, userEntity);
      });
    } catch (Exception e) {
      if (transaction != null) {
        transaction.rollback();
      }
      e.printStackTrace();
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

      Instant now = Instant.now();
      LocalDateTime localDateTime = LocalDateTime.ofInstant(now, ZoneId.systemDefault());
      order.setCreationDate(localDateTime);
      order.setStatus("Pending");
      orderRepository.save(session, order);
      transaction.commit();
      CompletableFuture.runAsync(() -> {
        processOrder(order, user);
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