package p.tecza.dcnds.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import p.tecza.dcnds.infrastructure.TicketMapper;
import p.tecza.dcnds.infrastructure.kafka.KafkaTicketProducer;
import p.tecza.dcnds.model.TextTicket;
import p.tecza.dcnds.model.TicketModel;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class FileService {

  private final EMLParser emlParser;
  private final KafkaTicketProducer kafkaTicketProducer;
  private final TicketMapper ticketMapper;

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

  public void createTicketBasedOnEmlFile(MultipartFile file) {
    TicketModel model = this.emlParser.createTicketModelFromEmlFile(file);
    this.ticketMapper.insertTicket(model);
    this.kafkaTicketProducer.publishTicket(model);
  }

}
