package p.tecza.dcnds.external.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class MailTicket {
  private List<From> from;
  private String id;
  private String subject;
  private String text;
  private LocalDateTime date;
}
