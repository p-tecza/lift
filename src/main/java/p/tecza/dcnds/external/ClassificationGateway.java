package p.tecza.dcnds.external;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import p.tecza.dcnds.model.MultiTicketClassificationResult;
import p.tecza.dcnds.model.MultiTicketModel;
import p.tecza.dcnds.model.TicketClassificationResult;
import p.tecza.dcnds.model.TicketModel;

import java.util.List;

@Service
@Slf4j
public class ClassificationGateway {

  @Value("${dcnds-net.host}")
  private String netHost;

  private final RestTemplate restTemplate = new RestTemplate();

  public TicketClassificationResult classificationOfSingleTicket(TicketModel ticketModel) {
    ResponseEntity<TicketClassificationResult> res =
      this.restTemplate.postForEntity(netHost + "/process-single", ticketModel, TicketClassificationResult.class);
    return res.getBody();
  }

  public MultiTicketClassificationResult classificationOfTicketBatch(MultiTicketModel multiTicketModel) {
    ResponseEntity<MultiTicketClassificationResult> res =
      this.restTemplate.postForEntity(netHost + "/process-multiple", multiTicketModel, MultiTicketClassificationResult.class);
    return res.getBody();
  }
}
