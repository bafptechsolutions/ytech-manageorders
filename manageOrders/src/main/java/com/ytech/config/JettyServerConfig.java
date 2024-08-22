package com.ytech.config;
//
//import com.ytech.exception.ConstraintViolationExceptionMapper;
//import org.eclipse.jetty.server.Server;
//import org.eclipse.jetty.servlet.ServletContextHandler;
//import org.eclipse.jetty.servlet.ServletHolder;
//import org.glassfish.jersey.jackson.JacksonFeature;
//import org.glassfish.jersey.server.ResourceConfig;
//import org.glassfish.jersey.servlet.ServletContainer;
//
///**
// * @author Bruno Pinto
// * @since 19/08/2024
// */
//public class JettyServerConfig {
//  public Server configureServer() {
//    Server server = new Server(8080);
//    ServletContextHandler context = new ServletContextHandler(ServletContextHandler.NO_SESSIONS);
//    context.setContextPath("/api/");
//    ResourceConfig config = new ResourceConfig();
//    config.register(new AppBinder());
//    config.packages("com.ytech.controller");
//    config.register(JacksonFeature.class);
//    config.register(ConstraintViolationExceptionMapper.class);
//    ServletContainer servletContainer = new ServletContainer(config);
//    ServletHolder servletHolder = new ServletHolder(servletContainer);
//    context.addServlet(servletHolder, "/*");
//    server.setHandler(context);
//    return server;
//  }
//}

import com.ytech.exception.ConstraintViolationExceptionMapper;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.glassfish.jersey.jackson.internal.jackson.jaxrs.json.JacksonJaxbJsonProvider;

public class JettyServerConfig {
  public Server configureServer() {
    // Cria o servidor Jetty na porta 8080
    Server server = new Server(8080);

    // Configura o contexto do servlet sem sessões
    ServletContextHandler context = new ServletContextHandler(ServletContextHandler.NO_SESSIONS);
    context.setContextPath("/api/");

    // Configura o Jersey com as classes e pacotes necessários
    ResourceConfig config = new ResourceConfig();
    config.register(new AppBinder()); // Binder para injeção de dependências
    config.packages("com.ytech.controller"); // Pacote que contém os controladores REST

    // Configura o Jackson para suportar Java 8 date/time types como LocalDateTime
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
    objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); // Para evitar timestamps numéricos

    // Registra o JacksonJaxbJsonProvider com o ObjectMapper configurado
    JacksonJaxbJsonProvider provider = new JacksonJaxbJsonProvider();
    provider.setMapper(objectMapper);
    config.register(provider);

    // Registra o Jackson como provedor de JSON
    config.register(JacksonFeature.class);

    // Registra o mapeador de exceções para validação
    config.register(ConstraintViolationExceptionMapper.class);

    // Configura o ServletContainer com a configuração do Jersey
    ServletContainer servletContainer = new ServletContainer(config);
    ServletHolder servletHolder = new ServletHolder(servletContainer);

    // Adiciona o ServletHolder ao contexto
    context.addServlet(servletHolder, "/*");

    // Define o contexto como o manipulador principal do servidor
    server.setHandler(context);

    // Retorna o servidor configurado
    return server;
  }
}
