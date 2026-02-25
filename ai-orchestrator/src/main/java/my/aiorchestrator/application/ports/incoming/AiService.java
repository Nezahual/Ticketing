package my.aiorchestrator.application.ports.incoming;

import java.io.IOException;
import my.aiorchestrator.domain.model.vos.*;

public interface AiService {

    OutTicketVO sendTicketToAiOrchestrator(InTicketVO ticketVO);

    OutReviewVO sendReviewToAiOrchestrator(InReviewVO reviewVO);

    OutQuestionVO askAQuestion(InQuestionVO questionVO) throws IOException;
}
