package com.ytech.config;

import com.ytech.controller.ItemController;
import com.ytech.repository.*;
import com.ytech.service.*;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.hibernate.SessionFactory;

/**
 * @author Bruno Pinto
 * @since 20/08/2024
 * Configuração de injeção da sessão e instâncias
 */
public class AppBinder extends AbstractBinder {
  @Override
  protected void configure() {
    SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

    ItemRepository itemRepository = new ItemRepository();
    OrderRepository orderRepository = new OrderRepository();
    OrderMovementRepository orderMovementRepository = new OrderMovementRepository();
    StockMovementRepository stockMovementRepository = new StockMovementRepository();
    UserRepository userRepository = new UserRepository();

    EmailService emailService = new EmailService();
    OrderMovementService orderMovementService = new OrderMovementService(orderMovementRepository, sessionFactory);
    UserService userService = new UserService(userRepository, sessionFactory);
    StockService stockService = new StockService(stockMovementRepository, sessionFactory);
    ProcessingOrdersService processingOrdersService = new ProcessingOrdersService(orderRepository, sessionFactory, stockService, emailService, orderMovementService, userService, stockMovementRepository);
    StockMovementService stockMovementService = new StockMovementService(stockMovementRepository, sessionFactory, processingOrdersService);
    ItemService itemService = new ItemService(itemRepository, sessionFactory, stockMovementService);
    OrderService orderService = new OrderService(orderRepository, sessionFactory, userService, processingOrdersService);

    bind(emailService).to(EmailService.class);
    bind(itemService).to(ItemService.class);
    bind(processingOrdersService).to(ProcessingOrdersService.class);
    bind(orderService).to(OrderService.class);
    bind(stockService).to(StockService.class);
    bind(orderMovementService).to(OrderMovementService.class);
    bind(stockMovementService).to(StockMovementService.class);
    bind(userService).to(UserService.class);

    // Para vincular os controladores ao container do HK2, por estarem indicados como @Service
    bind(ItemController.class).to(ItemController.class);
//    bind(itemService).to(ItemService.class);
//    bind(orderService).to(OrderService.class);
//    bind(orderMovementService).to(OrderMovementService.class);
//    bind(stockMovementService).to(StockMovementService.class);
//    bind(userService).to(UserService.class);
  }
}