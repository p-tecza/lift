package p.tecza.dcnds.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TicketContentWithResult {
  TicketModel ticket;
  String result;
}
