package my.aiorchestrator.application.ports.outgoing;

import java.io.IOException;
import my.aiorchestrator.domain.model.vos.*;

public interface AiOrchestrator {

    OutTicketVO sendTicketToLLM(InTicketVO ticketVO);

    OutReviewVO categorizeReviewFeeling(InReviewVO reviewVO);

    OutWeatherVO getWeatherForTravel(InWeatherVO weatherVO) throws IOException;
}
