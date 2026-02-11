package my.aiorchestrator.infrastructure.adapters;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.aiorchestrator.application.ports.outgoing.AiOrchestrator;
import my.aiorchestrator.application.ports.outgoing.ChromaService;
import my.aiorchestrator.configuration.AiModelsProperties;
import my.aiorchestrator.domain.model.vos.InReviewVO;
import my.aiorchestrator.domain.model.vos.InTicketVO;
import my.aiorchestrator.domain.model.vos.OutReviewVO;
import my.aiorchestrator.domain.model.vos.OutTicketVO;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AiOrchestratorAdapter implements AiOrchestrator {

    private final Map<String, ChatModel> chatModels;
    private final ChromaService chromaService;
    private final AiModelsProperties aiModelsProperties;

    @Value("${ai.defaultProvider}")
    private String defaultProvider;

    @Value("${ai.defaultProvider}")
    private String defaultModel;

    @Override
    public OutTicketVO sendTicketToLLM(InTicketVO ticketVO) {

        ChatModel model = this.loadModel(ticketVO.aiProvider(), ticketVO.aiModel());

        return null;
    }

    @Override
    public OutReviewVO categorizeReviewFeeling(InReviewVO reviewVO) {

        ChatModel model = this.loadModel(defaultProvider, defaultModel);

        return null;
    }

    private ChatModel loadModel(String provider, String model) {

        return this.chatModels.get(provider + ":" + model);
    }
}
