package p.tecza.dcnds.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketClassificationWrapper {
  private TicketClassificationResult classifiedTicket;
  private String finalClass;
  private boolean resolved;
}
