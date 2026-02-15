package my.aiorchestrator.application.ports.outgoing;

import my.aiorchestrator.domain.model.vos.*;

import java.io.IOException;

public interface AiOrchestrator {

    OutTicketVO sendTicketToLLM(InTicketVO ticketVO);

    OutReviewVO categorizeReviewFeeling(InReviewVO reviewVO);

    OutWeatherVO getWeatherForTravel(InWeatherVO weatherVO) throws IOException;
}
