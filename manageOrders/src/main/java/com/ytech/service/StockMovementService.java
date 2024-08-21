package com.ytech.service;

import com.ytech.model.StockMovementEntity;
import com.ytech.repository.StockMovementRepository;
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
public class StockMovementService {

  private final StockMovementRepository stockMovementRepository;
  private final SessionFactory sessionFactory;

  public StockMovementService(StockMovementRepository stockMovementRepository, SessionFactory sessionFactory) {
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
      return new ServiceResponse<>(null, Response.Status.INTERNAL_SERVER_ERROR);
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
      return new ServiceResponse<>(null, Response.Status.INTERNAL_SERVER_ERROR);
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
      return new ServiceResponse<>(null, Response.Status.INTERNAL_SERVER_ERROR);
    }
  }

  public ServiceResponse<StockMovementEntity> create(StockMovementEntity stockMovement) {
    Transaction transaction = null;
    try (Session session = sessionFactory.openSession()) {
      transaction = session.beginTransaction();
      stockMovementRepository.save(session, stockMovement);
      transaction.commit();
      return new ServiceResponse<>(stockMovement, Response.Status.CREATED);
    } catch (Exception e) {
      if (transaction != null) {
        transaction.rollback();
      }
      return new ServiceResponse<>(null, Response.Status.INTERNAL_SERVER_ERROR);
    }
  }

}
