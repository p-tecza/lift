package p.tecza.dcnds.infrastructure.kafka;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import p.tecza.dcnds.model.TicketModel;

@Configuration
public class KafkaConfig {

  @Bean
  public KafkaTemplate<String, TicketModel> kafkaTemplate(ProducerFactory<String, TicketModel> producerFactory) {
    return new KafkaTemplate<>(producerFactory);
  }

}
