package com.ytech.service;

import com.ytech.model.OrderEntity;
import com.ytech.model.UserEntity;
import org.apache.log4j.Logger;

/**
 * @author Bruno Pinto
 * @since 21/08/2024
 */
public class EmailService {

  private static final Logger logger = Logger.getLogger(EmailService.class);

  public void sendEmail(String toEmail, String subject, String body) {
    logger.info("Simulating email sending...");
    logger.info("To: " + toEmail);
    logger.info("Subject: " + subject);
    logger.info("Body: \n" + body);
    logger.info("Email simulation complete.");
  }

  public void sendOrderInformationToUser(OrderEntity orderEntity, UserEntity user) {
    try {
      String emailBody = "Hello, " + user.getName() + "!\n\n" +
          "Here's the status of your order :\n" +
          "Order ID: " + orderEntity.getId() +
          ", Item: " + orderEntity.getItemId() +
          ", Quantity: " + orderEntity.getQuantity() +
          ", Status: " + orderEntity.getStatus() +
          "\n";
      sendEmail(user.getEmail(), "Your Order Information", emailBody);
    } catch (Exception e) {
      throw new RuntimeException("Failed to send email", e);
    }
  }
}
