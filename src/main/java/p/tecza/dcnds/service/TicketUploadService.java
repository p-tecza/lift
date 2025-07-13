package p.tecza.dcnds.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import p.tecza.dcnds.infrastructure.TicketMapper;
import p.tecza.dcnds.infrastructure.kafka.KafkaTicketProducer;
import p.tecza.dcnds.model.TextTicket;
import p.tecza.dcnds.model.TicketModel;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class TicketUploadService {

  private final TicketMapper ticketMapper;
  private final FileService fileService;
  private final KafkaTicketProducer kafkaTicketProducer;

  public String handlePlainTextTicketUpload(TextTicket ticketText){
    if (ticketText == null) {
      return "Null ticket";
    }
    try {
      this.fileService.saveToFile(ticketText);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    TicketModel ticketModel = this.createTicketModel(ticketText);

    log.info("Received new ticket: {}", ticketText);

    this.ticketMapper.insertTicket(ticketModel);
    this.kafkaTicketProducer.publishTicket(ticketModel);
    return "ok";
  }

  private TicketModel createTicketModel(TextTicket ticketText) {
    return new TicketModel(
      UUID.randomUUID().toString(),
      ticketText.getSubject(),
      ticketText.getMessage(),
      ticketText.getEmail()
    );
  }


}
