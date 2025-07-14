package p.tecza.dcnds.infrastructure;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import p.tecza.dcnds.model.TicketClassificationResult;
import p.tecza.dcnds.model.TicketClassificationWrapper;

import java.util.List;

@Mapper
public interface TicketClassificationMapper {

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
