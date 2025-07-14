package p.tecza.dcnds.service;

import org.springframework.stereotype.Service;
import p.tecza.dcnds.model.TextTicket;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class FileService {

  public void saveToFile(TextTicket ticket) throws IOException {
    String filename = "ticket_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".txt";
    Path path = Paths.get("uploads/" + filename);

    Files.createDirectories(path.getParent());

    String content = String.format(
      "Email: %s%nSubject: %s%nMessage: %s%n",
      ticket.getEmail(),
      ticket.getSubject(),
      ticket.getMessage()
    );

    Files.write(path, content.getBytes(), StandardOpenOption.CREATE);
  }

}
