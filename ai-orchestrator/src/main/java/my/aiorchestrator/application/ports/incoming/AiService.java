package my.aiorchestrator.application.ports.incoming;

import my.aiorchestrator.domain.model.vos.*;

import java.io.IOException;

public interface AiService {

    OutTicketVO sendTicketToAiOrchestrator(InTicketVO ticketVO);

    OutReviewVO sendReviewToAiOrchestrator(InReviewVO reviewVO);

    OutWeatherVO sendTravelFileToAiOrchestrator(InWeatherVO weatherVO) throws IOException;
}
