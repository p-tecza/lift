package p.tecza.dcnds.contollers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import p.tecza.dcnds.service.FileService;
import p.tecza.dcnds.model.TextTicket;
import p.tecza.dcnds.service.TicketUploadService;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/text")
@RequiredArgsConstructor
public class TextUploadController {

  private final TicketUploadService ticketUploadService;


  @PostMapping("/upload")
  public ResponseEntity<String> handlePlainTextUpload(@RequestBody TextTicket ticketText) {
    String res = this.ticketUploadService.handlePlainTextTicketUpload(ticketText);
    return ResponseEntity.of(Optional.of(res));
  }

}
