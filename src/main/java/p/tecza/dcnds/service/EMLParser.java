package p.tecza.dcnds.service;

import jakarta.mail.*;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import p.tecza.dcnds.model.TicketModel;

import java.io.InputStream;
import java.util.Properties;
import java.util.UUID;

@Slf4j
@Service
public class EMLParser {

  public TicketModel createTicketModelFromEmlFile(MultipartFile file) {
    TicketModel ticket = null;
    try (InputStream fis = file.getInputStream()) {
      Session session = Session.getDefaultInstance(new Properties());
      MimeMessage message = new MimeMessage(session, fis);
      Address[] fromAddresses = message.getFrom();
      String sender = (fromAddresses != null && fromAddresses.length > 0)
        ? fromAddresses[0].toString() : "Sender not defined";
      String subject = message.getSubject();
      String sendDate = message.getSentDate().toString();
      String content = getTextFromMessage(message);

      log.info("emailSender: {}", sender);
      log.info("subject: {}", subject);
      log.info("sent: {}", sendDate);
      log.info("message: {}", content);
      ticket = new TicketModel(
        UUID.randomUUID().toString(),
        subject,
        content,
        sender
      );

    } catch (Exception e) {
      log.error("Unable to read EML file. {}", e.getMessage());
    }
    return ticket;
  }

  private static String getTextFromMessage(Message message) throws Exception {
    if (message.isMimeType("text/plain")) {
      return message.getContent().toString();
    } else if (message.isMimeType("multipart/*")) {
      Multipart multipart = (Multipart) message.getContent();
      for (int i = 0; i < multipart.getCount(); i++) {
        BodyPart bodyPart = multipart.getBodyPart(i);
        if (bodyPart.isMimeType("text/plain")) {
          return bodyPart.getContent().toString();
        } else if (bodyPart.isMimeType("text/html")) {
          return bodyPart.getContent().toString();
        }
      }
    }
    return "Unable to read content.";
  }
}