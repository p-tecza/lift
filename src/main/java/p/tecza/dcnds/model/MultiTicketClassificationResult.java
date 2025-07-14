package p.tecza.dcnds.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MultiTicketClassificationResult {
  private String error;
  private List<TicketClassificationResult> result;
}
