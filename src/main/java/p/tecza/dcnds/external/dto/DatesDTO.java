package p.tecza.dcnds.external.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DatesDTO {
  LocalDateTime from;
  LocalDateTime to;
}
