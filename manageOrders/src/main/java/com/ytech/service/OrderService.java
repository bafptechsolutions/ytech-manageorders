package com.ytech.service;

import com.ytech.dto.OrderDto;
import com.ytech.model.*;
import com.ytech.repository.ItemRepository;
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

/**
 * @author Bruno Pinto
 * @since 21/08/2024
 */
@Service
public class OrderService {

  private final OrderRepository orderRepository;
  private final SessionFactory sessionFactory;
  private final UserService userService;
  private final ProcessingOrdersService processingOrdersService;
  private final ItemRepository itemRepository;

  public OrderService(OrderRepository orderRepository, SessionFactory sessionFactory, UserService userService, ProcessingOrdersService processingOrdersService, ItemRepository itemRepository) {
    this.orderRepository = orderRepository;
    this.sessionFactory = sessionFactory;
    this.userService = userService;
    this.processingOrdersService = processingOrdersService;
    this.itemRepository = itemRepository;
  }

  public ServiceResponse<List<OrderDto>> findAll() {
    Transaction transaction = null;
    try (Session session = sessionFactory.openSession()) {
      transaction = session.beginTransaction();
      List<OrderEntity> orders = orderRepository.findAll(session);
      if (orders.isEmpty()) {
        return new ServiceResponse<>(new ArrayList<>(), Response.Status.NOT_FOUND);
      }
      List<OrderDto> orderDtos = new ArrayList<>();
      for (OrderEntity orderEntity : orders) {
        ItemEntity itemEntity = itemRepository.findById(session, orderEntity.getItemId());
        OrderDto orderDto = new OrderDto();
        orderDto.setId(orderEntity.getId());
        orderDto.setCreationDate(orderEntity.getCreationDate());
        orderDto.setStatus(orderEntity.getStatus());
        orderDto.setQuantity(orderEntity.getQuantity());
        orderDto.setItem(itemEntity);
        orderDtos.add(orderDto);
      }
      return new ServiceResponse<>(orderDtos, Response.Status.OK);
    } catch (Exception e) {
      if (transaction != null) {
        transaction.rollback();
      }
      return new ServiceResponse<>(Response.Status.INTERNAL_SERVER_ERROR);
    }
  }

  public ServiceResponse<OrderDto> findById(Long id) {
    Transaction transaction = null;
    try (Session session = sessionFactory.openSession()) {
      transaction = session.beginTransaction();
      OrderEntity orderEntity = orderRepository.findById(session, id);
      if (orderEntity == null) {
        return new ServiceResponse<>(Response.Status.NOT_FOUND);
      }
      ItemEntity itemEntity = itemRepository.findById(session, orderEntity.getItemId());
      OrderDto orderDto = new OrderDto();
      orderDto.setId(orderEntity.getId());
      orderDto.setCreationDate(orderEntity.getCreationDate());
      orderDto.setStatus(orderEntity.getStatus());
      orderDto.setQuantity(orderEntity.getQuantity());
      orderDto.setItem(itemEntity);

      return new ServiceResponse<>(orderDto, Response.Status.OK);
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
      if (!Objects.equals(order.getStatus(), "Pending")) {
        return new ServiceResponse<>("only allows you to delete the order if it is in pending status", Response.Status.BAD_REQUEST);
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
        processingOrdersService.processOrder(order, user);
      });
      return new ServiceResponse<>(order, Response.Status.OK);
    } catch (Exception e) {
      if (transaction != null) {
        transaction.rollback();
      }
      return new ServiceResponse<>(Response.Status.INTERNAL_SERVER_ERROR);
    }
  }
}