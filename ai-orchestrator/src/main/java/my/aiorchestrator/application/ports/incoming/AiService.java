package my.aiorchestrator.application.ports.incoming;

import my.aiorchestrator.domain.model.vos.InTicketVO;
import my.aiorchestrator.domain.model.vos.OutTicketVO;

public interface AiService {

    OutTicketVO sendTicketToAiOrchestrator(InTicketVO ticketVO);
}
