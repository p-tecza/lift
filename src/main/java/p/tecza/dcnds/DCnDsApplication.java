package p.tecza.dcnds;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.UUID;

@SpringBootApplication
@EnableScheduling
public class DCnDsApplication {

  public static void main(String[] args) {
    SpringApplication.run(DCnDsApplication.class, args);
  }


  @PostConstruct
  public void init() {
    System.out.println(UUID.randomUUID().toString());
  }

}
