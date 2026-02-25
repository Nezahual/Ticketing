package my.aiorchestrator.application.services;

import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.aiorchestrator.application.ports.incoming.AiService;
import my.aiorchestrator.application.ports.outgoing.AiOrchestrator;
import my.aiorchestrator.domain.model.vos.*;
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

    @Override
    public OutQuestionVO askAQuestion(InQuestionVO questionVO) throws IOException {

        return aiOrchestrator.askQuestionToLLm(questionVO);
    }
}
