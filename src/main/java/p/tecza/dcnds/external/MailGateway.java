package p.tecza.dcnds.external;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import p.tecza.dcnds.external.dto.MailTicket;

import java.util.List;

@Service
@Slf4j
public class MailGateway {
  RestTemplate restTemplate = new RestTemplate();

  private final String MAILDEV_API = "http://localhost:1080";

  public List<MailTicket> fetchAllMails() {
    ResponseEntity<List<MailTicket>> response = restTemplate.exchange(
      MAILDEV_API + "/email",
      HttpMethod.GET,
      null,
      new ParameterizedTypeReference<>() {
      }
    );

    return response.getBody();
  }

  public void deleteMailsFromInbox(List<String> ticketsToDelete) {
    try{
      ticketsToDelete.forEach(mailTicketId -> {
        restTemplate.exchange(
          MAILDEV_API + "/email/" + mailTicketId,
          HttpMethod.DELETE,
          null,
          new ParameterizedTypeReference<>() {
          });
        try {
          Thread.sleep(100);
        } catch (InterruptedException e) {
          throw new RuntimeException(e);
        }
      });
    } catch(Exception e){
      log.info("Error deleting mails from inbox: {}", e.getMessage());
    }

  }
}
