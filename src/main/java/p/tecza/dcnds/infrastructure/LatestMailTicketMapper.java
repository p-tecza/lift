package p.tecza.dcnds.infrastructure;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import p.tecza.dcnds.model.TicketModel;

import java.time.LocalDateTime;

@Mapper
public interface LatestMailTicketMapper {

  @Insert("""
    INSERT INTO LATEST_PROCESSED_MAIL_TICKET (arrive_date)
    VALUES (
    #{date}
    );
    """)
  void insertLastProcessedMailTicketDate(@Param("date") LocalDateTime date);


  @Select("""
    SELECT MAX(arrive_date) FROM LATEST_PROCESSED_MAIL_TICKET
    """)
  LocalDateTime getLatestProcessedMail();

}
