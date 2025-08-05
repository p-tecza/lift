package p.tecza.dcnds.external;


import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.stereotype.Service;

import java.util.Properties;
import java.util.UUID;

@Service
public class MailSender {

  public void sendSingleTestMail(){
    String to = "test@example.com";
    String from = "me@example.com";
    String host = "localhost";

    Properties properties = System.getProperties();
    properties.setProperty("mail.smtp.host", host);
    properties.setProperty("mail.smtp.port", "1025");

    Session session = Session.getDefaultInstance(properties);

    try {
      MimeMessage message = new MimeMessage(session);

      message.setFrom(new InternetAddress(from));
      message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
      message.setSubject("Hello from Java " + UUID.randomUUID());
      message.setText("["+UUID.randomUUID()+"] This is a test email sent to MailDev from Java.");

      Transport.send(message);

      message = new MimeMessage(session);

      message.setFrom(new InternetAddress(from));
      message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
      message.setSubject("LETS GOOOO");
      message.setText("["+UUID.randomUUID()+"] I NEED THIS CHANGE ASAP I BG YOU DO SOMETHING ABOUT IT!");

      Transport.send(message);

      message = new MimeMessage(session);

      message.setFrom(new InternetAddress(from));
      message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
      message.setSubject("I dont know what its like " + UUID.randomUUID());
      message.setText("["+UUID.randomUUID()+"] TO BE A SAD MAN TO BE A BAD MAN");

      Transport.send(message);

      System.out.println("Message sent successfully!");
    } catch (MessagingException mex) {
      mex.printStackTrace();
    }
  }


}
