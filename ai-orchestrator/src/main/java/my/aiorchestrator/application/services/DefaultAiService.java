package my.aiorchestrator.application.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.aiorchestrator.application.ports.incoming.AiService;
import my.aiorchestrator.application.ports.outgoing.AiOrchestrator;
import my.aiorchestrator.domain.model.vos.InReviewVO;
import my.aiorchestrator.domain.model.vos.InTicketVO;
import my.aiorchestrator.domain.model.vos.OutReviewVO;
import my.aiorchestrator.domain.model.vos.OutTicketVO;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class DefaultAiService implements AiService {

    private final AiOrchestrator aiOrchestrator;

    @Override
    public OutTicketVO sendTicketToAiOrchestrator(InTicketVO ticketVO) {

        return aiOrchestrator.sendTicketToLLM(ticketVO);
    }

    @Override
    public OutReviewVO sendReviewToAiOrchestrator(InReviewVO reviewVO) {

        return aiOrchestrator.categorizeReviewFeeling(reviewVO);
    }
}
