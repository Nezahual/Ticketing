package my.aiorchestrator.application.ports.outgoing;

import my.aiorchestrator.domain.model.vos.InReviewVO;
import my.aiorchestrator.domain.model.vos.InTicketVO;
import my.aiorchestrator.domain.model.vos.OutReviewVO;
import my.aiorchestrator.domain.model.vos.OutTicketVO;

public interface AiOrchestrator {

    OutTicketVO sendTicketToLLM(InTicketVO ticketVO);

    OutReviewVO categorizeReviewFeeling(InReviewVO reviewVO);
}
