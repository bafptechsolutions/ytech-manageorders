package com.ytech.config;

import com.ytech.controller.ItemController;
import com.ytech.repository.ItemRepository;
import com.ytech.service.ItemService;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.hibernate.SessionFactory;

/**
 * @author Bruno Pinto
 * @since 20/08/2024
 */
public class AppBinder extends AbstractBinder {
  @Override
  protected void configure() {
    // Configurar a injeção do SessionFactory
    SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

    // Criar instâncias dos repositórios
    ItemRepository itemRepository = new ItemRepository();
//    UserRepository userRepository = new UserRepository();

    // Criar instâncias dos serviços
    ItemService itemService = new ItemService(itemRepository, sessionFactory);
//    UserService userService = new UserService(userRepository, sessionFactory);

    // Vincular as instâncias dos serviços
    bind(itemService).to(ItemService.class);
//    bind(userService).to(UserService.class);

    // Vincular os controladores ao container do HK2
    bind(ItemController.class).to(ItemController.class);
//    bind(UserController.class).to(UserController.class);
  }
}