package p.tecza.dcnds.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Classification {
  double change;
  double problem;
  double request;
  @Override
  public String toString() {
    return "|Classification|" +
      "\nchange: " + change +
      "\nproblem: " + problem +
      "\nrequest: " + request;
  }
}
