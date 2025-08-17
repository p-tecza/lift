package p.tecza.dcnds.infrastructure.kafka;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import p.tecza.dcnds.model.TicketContentWithResult;
import p.tecza.dcnds.model.TicketModel;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaTicketProducer {

  private final static String IN_TOPIC_NAME = "it-tickets";
  private final static String OUT_TOPIC_NAME_HELP = "target-help";
  private final static String OUT_TOPIC_NAME_SUGGESTIONS = "target-suggestions";

  private final KafkaTemplate<String, TicketModel> kafkaTemplate;

  public void publishTicket(TicketModel ticket) {
    CompletableFuture<SendResult<String, TicketModel>> future =
      this.kafkaTemplate.send(IN_TOPIC_NAME, ticket.getId(), ticket);

    future.whenComplete((result, ex) -> {
      if (ex == null) {
        log.info("[KAFKA-PRODUCER] Message has been sent to partition: {}, offset: {}",
          result.getRecordMetadata().partition(),
          result.getRecordMetadata().offset());
      }
    });
  }

  public void publishTicketBasedOnCategorizationResult(List<TicketContentWithResult> dataToPublish) {
    dataToPublish.forEach(ticket -> {
      switch (ticket.getResult()){
        case "problem", "request" -> this.publishToTargetTopic(OUT_TOPIC_NAME_HELP, ticket);
        case "change" -> this.publishToTargetTopic(OUT_TOPIC_NAME_SUGGESTIONS, ticket);
      }
    });
  }

  private void publishToTargetTopic(String topicName, TicketContentWithResult dataToPublish) {
    CompletableFuture<SendResult<String, TicketModel>> future =
      this.kafkaTemplate.send(topicName, dataToPublish.getTicket().getId(), dataToPublish.getTicket());

    future.whenComplete((result, ex) -> {
      if (ex == null) {
        log.info("[KAFKA-PRODUCER] Message has been sent to FINAL partition: {}, offset: {}",
          result.getRecordMetadata().partition(),
          result.getRecordMetadata().offset());
      }
    });
  }

}
