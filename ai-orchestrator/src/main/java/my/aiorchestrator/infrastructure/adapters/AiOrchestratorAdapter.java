package my.aiorchestrator.infrastructure.adapters;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.aiorchestrator.application.ports.outgoing.AiOrchestrator;
import my.aiorchestrator.application.ports.outgoing.ChromaService;
import my.aiorchestrator.configuration.AiModelsConfig;
import my.aiorchestrator.domain.model.vos.InTicketVO;
import my.aiorchestrator.domain.model.vos.OutTicketVO;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AiOrchestratorAdapter implements AiOrchestrator {

    private final ChromaService chromaService;
    // private final ChatClient.Builder chatClient;
    private final AiModelsConfig aiModelsConfig;

    @Override
    public OutTicketVO sendTicketToLLM(InTicketVO ticketVO) {

        AiModelsConfig.AiConfig aiConfig =
                aiModelsConfig.getConfig(ticketVO.aiProvider(), ticketVO.aiModel());

        log.info(aiConfig.baseUrl());

        return null;
    }
}
