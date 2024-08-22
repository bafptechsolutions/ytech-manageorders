package com.ytech;

import com.ytech.config.JettyServerConfig;
import org.apache.log4j.Logger;
import org.eclipse.jetty.server.Server;

/**
 * @author Bruno Pinto
 * @since 18/08/2024
 */
public class Application {
  public static void main(String[] args) throws Exception {
    JettyServerConfig serverConfig = new JettyServerConfig();
    Server server = serverConfig.configureServer();
    try {
      server.start();
      server.join();
    } catch (Exception e) {
      e.printStackTrace();
      System.exit(1);
    }
  }
}

//port com.ytech.config.JettyServerConfig;
//import org.apache.log4j.Logger;
//import org.eclipse.jetty.server.Server;
//
///**
// * @author Bruno Pinto
// * @since 18/08/2024
// */
//public class Application {
//
//  private static final Logger logger = Logger.getLogger(Application.class);
//
//  public static void main(String[] args) throws Exception {
//    JettyServerConfig serverConfig = new JettyServerConfig();
//    Server server = serverConfig.configureServer();
//    try {
//      server.start();
//      server.join();
//    } catch (Exception e) {
//      logger.error("Error starting Jetty server", e);
//      System.exit(1);
//    }
//  }
//}