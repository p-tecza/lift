package p.tecza.dcnds.infrastructure;

import org.apache.ibatis.annotations.*;
import p.tecza.dcnds.external.dto.TicketContentDTO;
import p.tecza.dcnds.external.dto.TicketDTO;
import p.tecza.dcnds.model.TicketClassificationResult;
import p.tecza.dcnds.model.TicketClassificationWrapper;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface TicketClassificationMapper {

  @Select("""
        SELECT
            tc.message_uuid,
            tc.result,
            tc.created_at,
            t.subject,
            tc.prob_certainty,
            tc.req_certainty,
            tc.change_certainty
            -- inne potrzebne kolumny
        FROM TICKET_CLASSIFICATION tc
        INNER JOIN TICKET t ON tc.message_uuid = t.message_uuid
        WHERE tc.created_at BETWEEN #{from} AND #{to}
        ORDER BY created_at
""")
  @Results({
    @Result(property = "id", column = "message_uuid"),
    @Result(property = "result", column = "result"),
    @Result(property = "date", column = "created_at"),
    @Result(property = "subject", column = "subject"),
    @Result(property = "problemProb", column = "prob_certainty"),
    @Result(property = "reqProb", column = "req_certainty"),
    @Result(property = "changeProb", column = "change_certainty")
  })
  public List<TicketDTO> fetchTicketClassificationBetweenDates(@Param("from") LocalDateTime from,
                                                               @Param("to") LocalDateTime to);


  @Select("""
    SELECT t.subject, t.message, t.sender_email, t.created_at
    FROM TICKET t
    WHERE t.message_uuid = #{ticketId}
""")
  @Results({
    @Result(property = "senderEmail", column = "sender_email"),
    @Result(property = "createdAt", column = "created_at")
  })
  public TicketContentDTO fetchTicketContent(@Param("ticketId") String ticketId);

  @Insert("""
    INSERT INTO TICKET_CLASSIFICATION (message_uuid, result, prob_certainty, change_certainty, req_certainty, resolved)
    VALUES (
    #{classifiedTicket.id},
    #{finalClass},
    #{classifiedTicket.result.problem},
    #{classifiedTicket.result.change},
    #{classifiedTicket.result.request},
    #{resolved}
    );
    """)
  public void insertTicket(@Param("classifiedTicket") TicketClassificationResult classifiedTicked,
                           @Param("finalClass") String finalClass,
                           @Param("resolved") boolean resolved);


  @Insert("<script>" +
    "INSERT INTO TICKET_CLASSIFICATION (message_uuid, result, prob_certainty, change_certainty, req_certainty, resolved) " +
    "VALUES " +
    "<foreach collection='wrappedList' item='wrapped' index='index' separator=','>" +
    "(" +
    "#{wrapped.classifiedTicket.id}, " +
    "#{wrapped.finalClass}, " +
    "#{wrapped.classifiedTicket.result.problem}, " +
    "#{wrapped.classifiedTicket.result.change}, " +
    "#{wrapped.classifiedTicket.result.request}, " +
    "#{wrapped.resolved}" +
    ")" +
    "</foreach>" +
    "</script>")
  void insertTicketsBatch(@Param("wrappedList") List<TicketClassificationWrapper> items);

}
