package com.ytech.config;

import com.ytech.controller.*;
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

    LoggerService loggerService = new LoggerService();

    TraceService traceService = new TraceService(sessionFactory, orderRepository, userRepository, itemRepository, orderMovementRepository, stockMovementRepository);

    EmailService emailService = new EmailService();
    OrderMovementService orderMovementService = new OrderMovementService(orderMovementRepository, sessionFactory);
    UserService userService = new UserService(userRepository, sessionFactory, orderRepository, itemRepository);
    StockService stockService = new StockService(stockMovementRepository, sessionFactory);
    ProcessingOrdersService processingOrdersService = new ProcessingOrdersService(orderRepository, sessionFactory, stockService, emailService, orderMovementService, userService, stockMovementRepository, loggerService);
    StockMovementService stockMovementService = new StockMovementService(stockMovementRepository, sessionFactory, processingOrdersService, loggerService, itemRepository);
    ItemService itemService = new ItemService(itemRepository, sessionFactory, stockMovementService);
    OrderService orderService = new OrderService(orderRepository, sessionFactory, userService, processingOrdersService, itemRepository);

    bind(loggerService).to(LoggerService.class);
    bind(emailService).to(EmailService.class);
    bind(itemService).to(ItemService.class);
    bind(processingOrdersService).to(ProcessingOrdersService.class);
    bind(orderService).to(OrderService.class);
    bind(stockService).to(StockService.class);
    bind(orderMovementService).to(OrderMovementService.class);
    bind(stockMovementService).to(StockMovementService.class);
    bind(userService).to(UserService.class);
    bind(traceService).to(TraceService.class);

    // Para vincular os controladores ao container do HK2, por estarem indicados como @Service
    bind(ItemController.class).to(ItemController.class);
    bind(OrderController.class).to(OrderController.class);
    bind(OrderMovementController.class).to(OrderMovementController.class);
    bind(StockMovementController.class).to(StockMovementController.class);
    bind(UserController.class).to(UserController.class);
  }
}