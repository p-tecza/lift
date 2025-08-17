package p.tecza.dcnds.infrastructure.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import p.tecza.dcnds.model.TicketModel;
import p.tecza.dcnds.service.ClassificationService;

@Component
@Slf4j
@RequiredArgsConstructor
public class KafkaConsumer {

  private final ClassificationService classificationService;
  ObjectMapper objectMapper = new ObjectMapper();

  @KafkaListener(topics = "it-tickets", groupId = "dcnds")
  public void listen(String message) {
    TicketModel model;
    try {
      model = objectMapper.readValue(message, TicketModel.class);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
    log.info("[KAFKA-CONSUMER] Message received: {}", model);
    this.classificationService.resolveClassificationOfSingleTicket(model);
  }

}
