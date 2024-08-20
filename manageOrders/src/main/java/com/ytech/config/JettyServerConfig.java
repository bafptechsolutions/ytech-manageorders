package com.ytech.config;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;

/**
 * @author Bruno Pinto
 * @since 19/08/2024
 */
public class JettyServerConfig {
  public Server configureServer() {
    Server server = new Server(8080);
    ServletContextHandler context = new ServletContextHandler(ServletContextHandler.NO_SESSIONS);
    context.setContextPath("/api/");
    ResourceConfig config = new ResourceConfig();
    config.register(new AppBinder());
    config.packages("com.ytech.controller");
    ServletContainer servletContainer = new ServletContainer(config);
    ServletHolder servletHolder = new ServletHolder(servletContainer);
    context.addServlet(servletHolder, "/*");
    server.setHandler(context);
    return server;
  }
}