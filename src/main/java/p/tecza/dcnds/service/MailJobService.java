package p.tecza.dcnds.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import p.tecza.dcnds.external.MailGateway;
import p.tecza.dcnds.external.dto.From;
import p.tecza.dcnds.external.dto.MailTicket;
import p.tecza.dcnds.infrastructure.LatestMailTicketMapper;
import p.tecza.dcnds.infrastructure.TicketMapper;
import p.tecza.dcnds.model.TicketModel;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailJobService {

  private static final int MINUS_MINUTES = 1;
  private final MailGateway mailGateway;
  private final LatestMailTicketMapper latestMailTicketMapper;
  private final TicketMapper ticketMapper;
  private final ClassificationService classificationService;

  @Scheduled(fixedRate = 10000)
  public void fetchEmails() {
    try {
      List<MailTicket> mails = this.mailGateway.fetchAllMails();
      LocalDateTime lastReadDateTime = this.latestMailTicketMapper.getLatestProcessedMail();
      List<MailTicket> toProcess = new ArrayList<>();
      List<String> toRemoveFromInbox = new ArrayList<>();
      LocalDateTime newTimeToMark = null;
      for (MailTicket mail : mails) {
        LocalDateTime mailTime = mail.getDate();
        if (mailTime == null) continue;
        mailTime = mailTime.plusHours(2); // time zone bias
        if (mail.getDate().isAfter(lastReadDateTime)) {
          if (newTimeToMark == null || mailTime.isAfter(newTimeToMark)) {
            newTimeToMark = mailTime;
          }
          toProcess.add(mail);
        }
        if (mailTime.isBefore(LocalDateTime.now().minusMinutes(MINUS_MINUTES))) {
          toRemoveFromInbox.add(mail.getId());
        }
      }
      if (newTimeToMark != null) {
        log.info("Inserting new mail ticket timestamp to DB.");
        this.latestMailTicketMapper.insertLastProcessedMailTicketDate(newTimeToMark);
      }

      if (!toProcess.isEmpty()) {
        List<TicketModel> ticketModels = toProcess.stream().map(mt -> {
            From from = mt.getFrom().isEmpty() ? null : mt.getFrom().getFirst();
            return new TicketModel(
              UUID.randomUUID().toString(),
              mt.getSubject(),
              mt.getText(),
              from == null ? null : from.getAddress()
            );
          }
        ).toList();

        ticketModels.forEach(this.ticketMapper::insertTicket);
        this.classificationService.resolveClassificationOfTicketBatch(
          ticketModels
        );
      }
      if (!toRemoveFromInbox.isEmpty()) {
        this.mailGateway.deleteMailsFromInbox(toRemoveFromInbox);
      }


    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}

