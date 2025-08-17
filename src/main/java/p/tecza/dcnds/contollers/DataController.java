package p.tecza.dcnds.contollers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import p.tecza.dcnds.external.dto.DatesDTO;
import p.tecza.dcnds.external.dto.TicketContentDTO;
import p.tecza.dcnds.external.dto.TicketDTO;
import p.tecza.dcnds.service.ClassificationService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/data")
@RequiredArgsConstructor
public class DataController {

  private final ClassificationService classificationService;

  @PostMapping("/by-dates")
  public List<TicketDTO> fetchTicketsByDates(@RequestBody DatesDTO dates) {
    return this.classificationService.fetchTicketClassificationBetweenDates(
      dates.getFrom(),
      dates.getTo()
    );
  }

  @GetMapping("/{ticketId}")
  public TicketContentDTO fetchTicketContent(@PathVariable String ticketId) {
    return this.classificationService.fetchTicketContent(ticketId);
  }

}
