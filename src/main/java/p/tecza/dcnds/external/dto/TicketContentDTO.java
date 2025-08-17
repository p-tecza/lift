package p.tecza.dcnds.external.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TicketContentDTO {
  String senderEmail;
  String subject;
  String message;
  LocalDateTime createdAt;
}
