package my.aiorchestrator.infrastructure.adapters;

import my.aiorchestrator.application.ports.outgoing.AiOrchestrator;
import my.aiorchestrator.domain.model.vos.InTicketVO;
import my.aiorchestrator.domain.model.vos.OutTicketVO;
import org.springframework.stereotype.Service;

@Service
public class AiOrchestratorAdapter implements AiOrchestrator {

    @Override
    public OutTicketVO sendTicketToLLM(InTicketVO ticketVO) {
        return null;
    }
}
