package my.aiorchestrator.application.services;

import my.aiorchestrator.application.ports.incoming.AiService;
import my.aiorchestrator.domain.model.vos.InTicketVO;
import my.aiorchestrator.domain.model.vos.OutTicketVO;

public class DefaultAiService implements AiService {

    @Override
    public OutTicketVO sendTicketToAiOrchestrator(InTicketVO ticketVO) {
        return null;
    }
}
