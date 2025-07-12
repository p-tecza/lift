package p.tecza.dcnds.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketModel {
  private String messageUuid;
  private String subject;
  private String message;
  private String senderEmail;
}
