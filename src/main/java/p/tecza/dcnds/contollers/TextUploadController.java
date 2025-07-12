package p.tecza.dcnds.contollers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import p.tecza.dcnds.FileService;
import p.tecza.dcnds.infrastructure.TicketMapper;
import p.tecza.dcnds.model.TextTicket;
import p.tecza.dcnds.model.TicketModel;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/text")
public class TextUploadController {

  private final FileService fileService;
  @Autowired
  public TextUploadController(FileService fileService) {
    this.fileService = fileService;
  }

  @PostMapping("/upload")
  public ResponseEntity<String> handlePlainTextUpload(@RequestBody TextTicket ticketText) {
    if (ticketText == null) {
      return ResponseEntity.badRequest().body("Zawartosc zgloszenia = null");
    }
    try {
      this.fileService.saveToFile(ticketText);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return ResponseEntity.ok("ok");
  }

}
