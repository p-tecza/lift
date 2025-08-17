package p.tecza.dcnds.service;

import kotlin.Pair;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import p.tecza.dcnds.external.ClassificationGateway;
import p.tecza.dcnds.external.dto.TicketContentDTO;
import p.tecza.dcnds.external.dto.TicketDTO;
import p.tecza.dcnds.infrastructure.TicketClassificationMapper;
import p.tecza.dcnds.model.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ClassificationService {

  private final ClassificationGateway classificationGateway;
  private final TicketClassificationMapper ticketClassificationMapper;

  @Value("${dcnds-net.classify.resolve-threshold}")
  private double resolveThreshold;

  public List<TicketDTO> fetchTicketClassificationBetweenDates(LocalDateTime from, LocalDateTime to) {
    List<TicketDTO> res = this.ticketClassificationMapper.fetchTicketClassificationBetweenDates(
      from, to
    );
    return res;
  }

  public TicketContentDTO fetchTicketContent(String ticketId) {
    TicketContentDTO res = this.ticketClassificationMapper.fetchTicketContent(ticketId);
    return res;
  }

  /**
   * Use for batch processing, out of message queue scope.
   */
  public void resolveClassificationOfTicketBatch(List<TicketModel> tickets) {
    log.info("Trying to classify batch of {} tickets.", tickets.size());
    MultiTicketModel multiTicketModel = new MultiTicketModel(tickets);
    MultiTicketClassificationResult result = this.classificationGateway.classificationOfTicketBatch(multiTicketModel);
    if (result == null) throw new RuntimeException("Classifying failed");
    List<TicketClassificationWrapper> wrappedList = new ArrayList<>();
    result.getResult().forEach(singlePredict -> {
      log.info("Single classification result: {}", singlePredict);
      Pair<String, Double> predicted = this.getPredictedClass(singlePredict);
      boolean resolved = this.resolved(predicted.getSecond());
      wrappedList.add(
        new TicketClassificationWrapper(
          singlePredict,
          predicted.getFirst(),
          resolved
        )
      );
    });
    this.ticketClassificationMapper.insertTicketsBatch(wrappedList);
  }

  public void resolveClassificationOfSingleTicket(TicketModel ticketModel) {
    log.info("Trying to classify ticket with UUID: {}", ticketModel.getId());
    log.info("Ticket message: {}", ticketModel.getMessage());
    TicketClassificationResult result = this.classificationGateway.classificationOfSingleTicket(ticketModel);
    log.info("Classification result: {}", result);
    Pair<String, Double> predicted = this.getPredictedClass(result);
    boolean resolved = this.resolved(predicted.getSecond());
    log.info("Predicted class: {} | prob: {} | resolved : {}", predicted.getFirst(), predicted.getSecond(), resolved);
    this.ticketClassificationMapper.insertTicket(result, predicted.getFirst(), resolved);
  }

  private Pair<String, Double> getPredictedClass(TicketClassificationResult result) {
    Pair<String, Double> predictedClass = new Pair<>("change", result.getResult().getChange());
    if (result.getResult().getRequest() > predictedClass.getSecond()) {
      predictedClass = new Pair<>("request", result.getResult().getRequest());
    }
    if (result.getResult().getProblem() > predictedClass.getSecond()) {
      predictedClass = new Pair<>("problem", result.getResult().getProblem());
    }
    return predictedClass;
  }

  private boolean resolved(Double val) {
    return val >= resolveThreshold / 100;
  }

}
