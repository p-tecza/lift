package p.tecza.dcnds.contollers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import p.tecza.dcnds.external.MailSender;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mail")
@Slf4j
public class MailController {

  private final MailSender mailSender;

  @PostMapping()
  public void sendMail() {
    log.info("Sending single test mail");
    this.mailSender.sendSingleTestMail();
  }

}
