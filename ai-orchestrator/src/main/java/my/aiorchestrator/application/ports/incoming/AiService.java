package my.aiorchestrator.application.ports.incoming;

import my.aiorchestrator.domain.model.vos.InReviewVO;
import my.aiorchestrator.domain.model.vos.InTicketVO;
import my.aiorchestrator.domain.model.vos.OutReviewVO;
import my.aiorchestrator.domain.model.vos.OutTicketVO;

public interface AiService {
    // test
    OutTicketVO sendTicketToAiOrchestrator(InTicketVO ticketVO);

    OutReviewVO sendReviewToAiOrchestrator(InReviewVO reviewVO);
}
