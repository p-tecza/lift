package p.tecza.dcnds.infrastructure.kafka;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import p.tecza.dcnds.model.TicketModel;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaTicketProducer {

  private final KafkaTemplate<String, TicketModel> kafkaTemplate;

  public void publishTicket(TicketModel ticket) {
    CompletableFuture<SendResult<String, TicketModel>> future =
      this.kafkaTemplate.send("it-tickets", ticket.getId(), ticket);

    future.whenComplete((result, ex) -> {
      if (ex == null) {
        log.info("[KAFKA-PRODUCER] Message has been sent to partition: {}, offset: {}",
          result.getRecordMetadata().partition(),
          result.getRecordMetadata().offset());
      }
    });

  }

}
