package p.tecza.dcnds.infrastructure;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import p.tecza.dcnds.model.TicketFileModel;
import p.tecza.dcnds.model.TicketModel;

@Mapper
public interface TicketMapper {

  @Insert("""
    INSERT INTO TICKET (message_uuid, subject, message, sender_email)
    VALUES (
    #{ticket.id},
    #{ticket.subject},
    #{ticket.message},
    #{ticket.senderEmail}
    );
    """)
  public void insertTicket(@Param("ticket") TicketModel ticket);

  @Insert("""
    INSERT INTO TICKET_FILE (message_uuid, file_path, file_name, file_type)
    VALUES (
    #{ticketUuid},
    #{ticketFile.filePath},
    #{ticketFile.fileName},
    #{ticketFile.fileType}
    );
    """)
  public void insertTicketFile(@Param("ticketFile") TicketFileModel ticketFile, @Param("ticketUuid") String ticketUuid);

}
