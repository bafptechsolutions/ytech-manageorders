package com.ytech.service;

import com.ytech.model.ItemEntity;
import com.ytech.model.StockMovementEntity;
import com.ytech.repository.StockMovementRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.jvnet.hk2.annotations.Service;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * @author Bruno Pinto
 * @since 21/08/2024
 */
@Service
public class StockMovementService {

  private final StockMovementRepository stockMovementRepository;
  private final SessionFactory sessionFactory;
  private final ItemService itemService;
  private final OrderMovementService orderMovementService;

  public StockMovementService(StockMovementRepository stockMovementRepository, SessionFactory sessionFactory, ItemService itemService, OrderMovementService orderMovementService) {
    this.stockMovementRepository = stockMovementRepository;
    this.sessionFactory = sessionFactory;
    this.itemService = itemService;
    this.orderMovementService = orderMovementService;
  }

  public ServiceResponse<List<StockMovementEntity>> findAll() {
    try (Session session = sessionFactory.openSession()) {
      List<StockMovementEntity> stockMovements = stockMovementRepository.findAll(session);
      if (stockMovements.isEmpty()) {
        return new ServiceResponse<>(new ArrayList<>(), Response.Status.NOT_FOUND);
      }
      return new ServiceResponse<>(stockMovements, Response.Status.OK);
    } catch (Exception e) {
      return new ServiceResponse<>(Response.Status.INTERNAL_SERVER_ERROR);
    }
  }

  public ServiceResponse<List<StockMovementEntity>> findAllByItemId(Long itemId) {
    try (Session session = sessionFactory.openSession()) {
      List<StockMovementEntity> stockMovements = stockMovementRepository.findAllByItemId(session, itemId);
      if (stockMovements.isEmpty()) {
        return new ServiceResponse<>(new ArrayList<>(), Response.Status.NOT_FOUND);
      }
      return new ServiceResponse<>(stockMovements, Response.Status.OK);
    } catch (Exception e) {
      return new ServiceResponse<>(Response.Status.INTERNAL_SERVER_ERROR);
    }
  }

  public ServiceResponse<StockMovementEntity> findById(Long id) {
    try (Session session = sessionFactory.openSession()) {
      StockMovementEntity stockMovement = stockMovementRepository.findById(session, id);
      if (stockMovement == null) {
        return new ServiceResponse<>(new StockMovementEntity(), Response.Status.NOT_FOUND);
      }
      return new ServiceResponse<>(stockMovement, Response.Status.OK);
    } catch (Exception e) {
      return new ServiceResponse<>(Response.Status.INTERNAL_SERVER_ERROR);
    }
  }

  public boolean hasSufficientStock(Session session, Long itemId, int requiredQuantity) {
    long totalStock = stockMovementRepository.getCurrentStockForItem(session, itemId);
    return totalStock >= requiredQuantity;
  }

  public ServiceResponse<StockMovementEntity> createStockMovement(StockMovementEntity stockMovement) {
    Transaction transaction = null;
    try (Session session = sessionFactory.openSession()) {
      transaction = session.beginTransaction();
      ItemEntity item = session.get(ItemEntity.class, stockMovement.getItemId());
      if (item == null) {
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("itemId", "does not exist");
        return new ServiceResponse<>(responseBody, Response.Status.NOT_FOUND);
      }
      itemService.updateStockQuantity(session, item, item.getQuantityInStock() + stockMovement.getQuantity());
      stockMovementRepository.save(session, stockMovement);
      transaction.commit();

      CompletableFuture.runAsync(() -> {
        orderMovementService.processPendingOrders(stockMovement);
      });
      return new ServiceResponse<>(stockMovement, Response.Status.CREATED);
    } catch (Exception e) {
      if (transaction != null) {
        transaction.rollback();
      }
      return new ServiceResponse<>(Response.Status.INTERNAL_SERVER_ERROR);
    }
  }
}
