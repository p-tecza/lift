package p.tecza.dcnds.external.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TicketDTO {
  String id;
  String subject;
  String result;
  double problemProb;
  double changeProb;
  double reqProb;
  LocalDateTime date;
}
