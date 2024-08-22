package com.ytech.service;

import com.ytech.dto.TraceOrderDto;
import com.ytech.dto.TraceOrderMovementDto;
import com.ytech.dto.TraceStockMovementDto;
import com.ytech.model.*;
import com.ytech.repository.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.jvnet.hk2.annotations.Service;

import javax.ws.rs.core.Response;
import java.util.List;

/**
 * @author Bruno Pinto
 * @since 22/08/2024
 */
@Service
public class TraceService {

  private final SessionFactory sessionFactory;
  private final OrderRepository orderRepository;
  private final UserRepository userRepository;
  private final ItemRepository itemRepository;
  private final OrderMovementRepository orderMovementRepository;
  private final StockMovementRepository stockMovementRepository;

  public TraceService(SessionFactory sessionFactory, OrderRepository orderRepository, UserRepository userRepository, ItemRepository itemRepository, OrderMovementRepository orderMovementRepository, StockMovementRepository stockMovementRepository) {
    this.sessionFactory = sessionFactory;
    this.orderRepository = orderRepository;
    this.userRepository = userRepository;
    this.itemRepository = itemRepository;
    this.orderMovementRepository = orderMovementRepository;
    this.stockMovementRepository = stockMovementRepository;
  }

  public ServiceResponse<TraceOrderDto> traceOrderMovementById(Long orderId) {
    Transaction transaction = null;
    try (Session session = sessionFactory.openSession()) {
      transaction = session.beginTransaction();

      OrderEntity orderEntity = orderRepository.findById(session, orderId);
      if (orderEntity == null) {
        return new ServiceResponse<>(Response.Status.NOT_FOUND);
      }

      TraceOrderDto traceOrderDto = new TraceOrderDto();
      traceOrderDto.setId(orderEntity.getId());
      traceOrderDto.setCreationDate(orderEntity.getCreationDate());
      traceOrderDto.setStatus(orderEntity.getStatus());
      traceOrderDto.setQuantity(orderEntity.getQuantity());

      UserEntity userEntity = userRepository.findById(session, orderEntity.getUserId());
      traceOrderDto.setUser(userEntity);

      ItemEntity itemEntity = itemRepository.findById(session, orderEntity.getItemId());
      traceOrderDto.setItem(itemEntity);

      List<OrderMovementEntity> orderMovements = orderMovementRepository.getAllByOrderId(session, orderEntity.getId());

      for (OrderMovementEntity orderMovementEntity : orderMovements) {
        TraceOrderMovementDto traceOrderMovementDto = new TraceOrderMovementDto();
        traceOrderMovementDto.setId(orderMovementEntity.getId());
        traceOrderMovementDto.setQuantityUsed(orderMovementEntity.getQuantityUsed());
        StockMovementEntity stockMovementEntity = stockMovementRepository.findById(session, orderMovementEntity.getStockMovementId());
        traceOrderMovementDto.setStockMovement(stockMovementEntity);
        traceOrderDto.addOrderMovement(traceOrderMovementDto);
      }

      return new ServiceResponse<>(traceOrderDto, Response.Status.OK);
    } catch (Exception e) {
      if (transaction != null) {
        transaction.rollback();
      }
      return new ServiceResponse<>(Response.Status.INTERNAL_SERVER_ERROR);
    }
  }

  public ServiceResponse<TraceStockMovementDto> traceStockMovementById(Long stockMovementId) {
    Transaction transaction = null;
    try (Session session = sessionFactory.openSession()) {
      transaction = session.beginTransaction();

      System.out.println("---------------------------------------- 01");
      StockMovementEntity stockMovementEntity = stockMovementRepository.findById(session, stockMovementId);
      System.out.println("---------------------------------------- 02");
      if (stockMovementEntity == null) {
        return new ServiceResponse<>(Response.Status.NOT_FOUND);
      }
      System.out.println("---------------------------------------- 03");

      TraceStockMovementDto traceStockMovementDto = new TraceStockMovementDto();
      traceStockMovementDto.setId(stockMovementEntity.getId());
      traceStockMovementDto.setQuantity(stockMovementEntity.getQuantity());
      traceStockMovementDto.setCreationDate(stockMovementEntity.getCreationDate());
      traceStockMovementDto.setRemainingQuantity(stockMovementEntity.getRemainingQuantity());

      List<OrderMovementEntity> orderMovements = orderMovementRepository.getAllByStockMovementId(session, stockMovementEntity.getId());

      for (OrderMovementEntity orderMovementEntity : orderMovements) {
        TraceOrderMovementDto traceOrderMovementDto = new TraceOrderMovementDto();
        traceOrderMovementDto.setId(orderMovementEntity.getId());
        traceOrderMovementDto.setQuantityUsed(orderMovementEntity.getQuantityUsed());

        OrderEntity orderEntity = orderRepository.findById(session, stockMovementId);

        TraceOrderDto traceOrderDto = new TraceOrderDto();
        traceOrderMovementDto.setId(orderEntity.getId());
        traceOrderDto.setCreationDate(orderEntity.getCreationDate());
        traceOrderDto.setStatus(orderEntity.getStatus());
        traceOrderDto.setQuantity(orderEntity.getQuantity());

        UserEntity userEntity = userRepository.findById(session, orderEntity.getUserId());
        traceOrderDto.setUser(userEntity);

        ItemEntity itemEntity = itemRepository.findById(session, orderEntity.getItemId());
        traceOrderDto.setItem(itemEntity);

        traceOrderMovementDto.setTraceOrder(traceOrderDto);
        traceStockMovementDto.addOrderMovement(traceOrderMovementDto);
      }

      return new ServiceResponse<>(traceStockMovementDto, Response.Status.OK);
    } catch (Exception e) {
      if (transaction != null) {
        transaction.rollback();
      }
      return new ServiceResponse<>(Response.Status.INTERNAL_SERVER_ERROR);
    }
  }
}
