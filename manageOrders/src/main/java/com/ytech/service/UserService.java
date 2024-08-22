package com.ytech.service;

import com.ytech.dto.OrderDto;
import com.ytech.dto.TraceOrderDto;
import com.ytech.model.ItemEntity;
import com.ytech.model.OrderEntity;
import com.ytech.model.StockMovementEntity;
import com.ytech.model.UserEntity;
import com.ytech.repository.ItemRepository;
import com.ytech.repository.OrderRepository;
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
  private final OrderRepository orderRepository;
  private final ItemRepository itemRepository;

  public UserService(UserRepository userRepository, SessionFactory sessionFactory, OrderRepository orderRepository, ItemRepository itemRepository) {
    this.userRepository = userRepository;
    this.sessionFactory = sessionFactory;
    this.orderRepository = orderRepository;
    this.itemRepository = itemRepository;
  }

  public ServiceResponse<List<UserEntity>> findAll() {
    Transaction transaction = null;
    try (Session session = sessionFactory.openSession()) {
      transaction = session.beginTransaction();
      List<UserEntity> users = userRepository.findAll(session);
      if (users.isEmpty()) {
        return new ServiceResponse<>(new ArrayList<>(), Response.Status.NOT_FOUND);
      }
      return new ServiceResponse<>(users, Response.Status.OK);
    } catch (Exception e) {
      if (transaction != null) {
        transaction.rollback();
      }
      return new ServiceResponse<>(Response.Status.INTERNAL_SERVER_ERROR);
    }
  }

  public ServiceResponse<UserEntity> findById(Long id) {
    Transaction transaction = null;
    try (Session session = sessionFactory.openSession()) {
      transaction = session.beginTransaction();
      UserEntity user = userRepository.findById(session, id);
      if (user == null) {
        return new ServiceResponse<>(new UserEntity(), Response.Status.NOT_FOUND);
      }
      return new ServiceResponse<>(user, Response.Status.OK);
    } catch (Exception e) {
      if (transaction != null) {
        transaction.rollback();
      }
      return new ServiceResponse<>(Response.Status.INTERNAL_SERVER_ERROR);
    }
  }

  public UserEntity findById(Session session, Long id) {
    return userRepository.findById(session, id);
  }

  public ServiceResponse<List<OrderDto>> findAllOrdersById(Long id) {
    Transaction transaction = null;
    try (Session session = sessionFactory.openSession()) {
      transaction = session.beginTransaction();
      UserEntity user = userRepository.findById(session, id);
      if (user == null) {
        return new ServiceResponse<>("user not found", Response.Status.NOT_FOUND);
      }
      List<OrderEntity> orders = orderRepository.findAllByUserId(session, user.getId());
      if (orders.isEmpty()) {
        return new ServiceResponse<>(Response.Status.NOT_FOUND);
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

  public ServiceResponse<UserEntity> create(UserEntity user) {
    Transaction transaction = null;
    try (Session session = sessionFactory.openSession()) {
      transaction = session.beginTransaction();
      if (userRepository.userExistsByName(session, user.getName())) {
        return new ServiceResponse<>("name already registered", Response.Status.BAD_REQUEST);
      }
      if (userRepository.userExistsByEmail(session, user.getEmail())) {
        return new ServiceResponse<>("email already registered", Response.Status.BAD_REQUEST);
      }
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

}
