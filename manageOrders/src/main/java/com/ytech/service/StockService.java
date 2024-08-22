package com.ytech.service;

import com.ytech.model.StockMovementEntity;
import com.ytech.repository.StockMovementRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.jvnet.hk2.annotations.Service;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Bruno Pinto
 * @since 21/08/2024
 */
@Service
public class StockService {

  private final StockMovementRepository stockMovementRepository;
  private final SessionFactory sessionFactory;

  public StockService(StockMovementRepository stockMovementRepository, SessionFactory sessionFactory) {
    this.stockMovementRepository = stockMovementRepository;
    this.sessionFactory = sessionFactory;
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

  public List<StockMovementEntity> allExistingStocksByItemId(Session session, Long itemId) {
    return stockMovementRepository.allExistingStocksByItemId(session, itemId);
  }

}
