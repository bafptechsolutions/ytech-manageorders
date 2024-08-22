package com.ytech.config;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * @author Bruno Pinto
 * @since 19/08/2024
 */
public class HibernateUtil {
  private static final SessionFactory sessionFactory;
  static {
    try {
      sessionFactory = new Configuration().configure().buildSessionFactory();
    } catch (Throwable ex) {
      System.err.println("Initial SessionFactory creation failed." + ex);
      throw new ExceptionInInitializerError(ex);
    }
  }
  public static SessionFactory getSessionFactory() {
    return sessionFactory;
  }
  public static void shutdown() {
    getSessionFactory().close();
  }
}