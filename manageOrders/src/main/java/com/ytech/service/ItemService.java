package com.ytech.service;

import com.ytech.model.ItemEntity;
import com.ytech.repository.ItemRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.jvnet.hk2.annotations.Service;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Bruno Pinto
 * @since 19/08/2024
 */
@Service
public class ItemService {

   private final ItemRepository itemRepository;
   private final SessionFactory sessionFactory;
   private final StockMovementService stockMovementService;

   public ItemService(ItemRepository itemRepository, SessionFactory sessionFactory, StockMovementService stockMovementService) {
      this.itemRepository = itemRepository;
      this.sessionFactory = sessionFactory;
     this.stockMovementService = stockMovementService;
   }

   public ServiceResponse<List<ItemEntity>> findAll() {
      Transaction transaction = null;
      try (Session session = sessionFactory.openSession()) {
         transaction = session.beginTransaction();
         List<ItemEntity> items = itemRepository.findAll(session);
         if (items.isEmpty()) {
            return new ServiceResponse<>(new ArrayList<>(), Response.Status.NOT_FOUND);
         }
         return new ServiceResponse<>(items, Response.Status.OK);
      } catch (Exception e) {
         if (transaction != null) {
            transaction.rollback();
         }
         return new ServiceResponse<>(Response.Status.INTERNAL_SERVER_ERROR);
      }
   }

   public ServiceResponse<ItemEntity> findById(Long id) {
      Transaction transaction = null;
      try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
         ItemEntity item = itemRepository.findById(session, id);
         if (item == null) {
            return new ServiceResponse<>(new ItemEntity(), Response.Status.NOT_FOUND);
         }
         return new ServiceResponse<>(item, Response.Status.OK);
      } catch (Exception e) {
         if (transaction != null) {
            transaction.rollback();
         }
         return new ServiceResponse<>(Response.Status.INTERNAL_SERVER_ERROR);
      }
   }

   public ServiceResponse<ItemEntity> create(ItemEntity item) {
      Transaction transaction = null;
      try (Session session = sessionFactory.openSession()) {
         transaction = session.beginTransaction();
         itemRepository.save(session, item);
         transaction.commit();
         return new ServiceResponse<>(item, Response.Status.CREATED);
      } catch (Exception e) {
         if (transaction != null) {
            transaction.rollback();
         }
         return new ServiceResponse<>(Response.Status.INTERNAL_SERVER_ERROR);
      }
   }

   public ServiceResponse<ItemEntity> update(Long id, ItemEntity item) {
      Transaction transaction = null;
      try (Session session = sessionFactory.openSession()) {
         transaction = session.beginTransaction();
         ItemEntity existingItem = itemRepository.findById(session, id);
         if (existingItem == null) {
            return new ServiceResponse<>(new ItemEntity(), Response.Status.NOT_FOUND);
         }
         existingItem.setName(item.getName());
         itemRepository.save(session, existingItem);
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
         ItemEntity item = itemRepository.findById(session, id);
         if (item == null) {
            return new ServiceResponse<>(Response.Status.NOT_FOUND);
         }
         if (stockMovementService.existsStockMovementItem(session, id)){
            return new ServiceResponse<>("You can only delete items that have never been moved in stock", Response.Status.BAD_REQUEST);
         }
         itemRepository.delete(session, item);
         transaction.commit();
         return new ServiceResponse<>(Response.Status.NO_CONTENT);
      } catch (Exception e) {
         if (transaction != null) {
            transaction.rollback();
         }
         return new ServiceResponse<>(Response.Status.INTERNAL_SERVER_ERROR);
      }
   }
}