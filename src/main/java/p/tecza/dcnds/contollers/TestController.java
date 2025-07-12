package p.tecza.dcnds.contollers;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import p.tecza.dcnds.infrastructure.TicketMapper;
import p.tecza.dcnds.model.TicketFileModel;
import p.tecza.dcnds.model.TicketModel;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
public class TestController {

  private final TicketMapper ticketMapper;

  @GetMapping("/ticket")
  public ResponseEntity<String> test() {

    this.ticketMapper.insertTicket(new TicketModel(
      UUID.randomUUID().toString(),
      "bleble",
      "eoeoeoeoeoeoeoe",
      "hoho@oh.tv"
    ));

    return ResponseEntity.ok("ok");
  }

  @GetMapping("/ticket-file/{uuid}")
  public ResponseEntity<String> testfile(@PathVariable("uuid") String uuid) {

    this.ticketMapper.insertTicketFile(new TicketFileModel(
        "/home/dir/",
        "text.eml",
        "EML"
      ),
      uuid
    );

    return ResponseEntity.ok("ok");
  }


}
