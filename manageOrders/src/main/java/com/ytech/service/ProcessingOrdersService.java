package com.ytech.service;

import com.ytech.model.OrderEntity;
import com.ytech.model.OrderMovementEntity;
import com.ytech.model.StockMovementEntity;
import com.ytech.model.UserEntity;
import com.ytech.repository.OrderRepository;
import com.ytech.repository.StockMovementRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.jvnet.hk2.annotations.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @author Bruno Pinto
 * @since 22/08/2024
 */
@Service
public class ProcessingOrdersService {

  private final OrderRepository orderRepository;
  private final SessionFactory sessionFactory;
  private final StockService stockService;
  private final EmailService emailService;
  private final OrderMovementService orderMovementService;
  private final UserService userService;
  private final StockMovementRepository stockMovementRepository;
  private final LoggerService loggerService;

  public ProcessingOrdersService(OrderRepository orderRepository, SessionFactory sessionFactory, StockService stockService, EmailService emailService, OrderMovementService orderMovementService, UserService userService, StockMovementRepository stockMovementRepository, LoggerService loggerService) {
    this.orderRepository = orderRepository;
    this.sessionFactory = sessionFactory;
    this.stockService = stockService;
    this.emailService = emailService;
    this.orderMovementService = orderMovementService;
    this.userService = userService;
    this.stockMovementRepository = stockMovementRepository;
    this.loggerService = loggerService;
  }

  public boolean processOrder(OrderEntity orderEntity, UserEntity userEntity) {
    Transaction transaction = null;
    try (Session session = sessionFactory.openSession()) {
      transaction = session.beginTransaction();
      if (stockService.hasSufficientStock(session, orderEntity.getItemId(), orderEntity.getQuantity())) {

        List<StockMovementEntity> stockMovements = stockService.allExistingStocksByItemId(session, orderEntity.getItemId());

        int remainingQuantity = orderEntity.getQuantity();
        for (StockMovementEntity stockMovementEntity : stockMovements) {
          if (remainingQuantity <= 0) {
            break;
          }

          int availableQuantity = stockMovementEntity.getRemainingQuantity();

          OrderMovementEntity orderMovementEntity = new OrderMovementEntity();
          orderMovementEntity.setOrderId(orderEntity.getId());
          orderMovementEntity.setStockMovementId(stockMovementEntity.getId());

          if (availableQuantity >= remainingQuantity) {
            // Se a quantidade no registo stock for suficiente para satisfazer o pedido na totalidade
            int usedQuantity = remainingQuantity;
            int newQuantity = availableQuantity - usedQuantity;
            stockMovementEntity.setRemainingQuantity(newQuantity);
            orderMovementEntity.setQuantityUsed(usedQuantity);
            remainingQuantity = 0;
          } else {
            // Se a quantidade no registo stock não for suficiente para satisfazer o pedido na totalidade usa a quantidade existente e passa para o p´roximo
            int usedQuantity = availableQuantity;
            remainingQuantity -= usedQuantity;
            stockMovementEntity.setRemainingQuantity(0);
            orderMovementEntity.setQuantityUsed(remainingQuantity);
          }
          stockMovementRepository.save(session, stockMovementEntity);
          orderMovementService.save(session, orderMovementEntity);
        }

        /*
          Caso durante o processamento concorrente já não exista quantidade suficiente
         */
        if (remainingQuantity > 0) {
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
      loggerService.logError(e);
    }
    return true;
  }

    public void processPendingOrders(StockMovementEntity stockMovement) {
      Transaction transaction = null;
      try (Session session = sessionFactory.openSession()) {
        transaction = session.beginTransaction();

        List<OrderEntity> pendingOrders = orderRepository.allPendingByItemId(session, stockMovement.getItemId());

        for (OrderEntity orderEntity : pendingOrders) {
          if (stockService.hasSufficientStock(session, stockMovement.getItemId(), orderEntity.getQuantity())) {
            UserEntity userEntity = userService.findById(session, orderEntity.getUserId());
            processOrder(orderEntity, userEntity);
          }
        }
      } catch (Exception e) {
        if (transaction != null) {
          transaction.rollback();
        }
        loggerService.logError(e);
      }
  }
}