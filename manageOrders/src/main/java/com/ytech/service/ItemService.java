package com.ytech.service;

import com.ytech.model.ItemEntity;
import com.ytech.repository.ItemRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.jvnet.hk2.annotations.Service;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Bruno Pinto
 * @since 19/08/2024
 */
@Service
public class ItemService {

   private final ItemRepository itemRepository;
   private final SessionFactory sessionFactory;

   public ItemService(ItemRepository itemRepository, SessionFactory sessionFactory) {
      this.itemRepository = itemRepository;
      this.sessionFactory = sessionFactory;
   }

   public ServiceResponse<Collection<ItemEntity>> all() {
      Transaction transaction = null;
      try (Session session = sessionFactory.openSession()) {
         transaction = session.beginTransaction();
         List<ItemEntity> items = itemRepository.getAll(session);
         transaction.commit();
         if (items.isEmpty()) {
            return new ServiceResponse<>(new ArrayList<>(), Response.Status.NOT_FOUND);
         }
         return new ServiceResponse<>(items, Response.Status.OK);
      } catch (Exception e) {
         if (transaction != null) {
            transaction.rollback();
         }
         return new ServiceResponse<>(null, Response.Status.INTERNAL_SERVER_ERROR);
      }
   }
}