package p.tecza.dcnds.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TicketClassificationResult {
  private String id;
  private String error;
  private Classification result;
}
