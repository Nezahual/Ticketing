package my.aiorchestrator.interfaces.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.aiorchestrator.application.ports.incoming.AiService;
import my.aiorchestrator.domain.model.vos.InReviewVO;
import my.aiorchestrator.domain.model.vos.InTicketVO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class TestController {

    private final AiService aiService;

    @GetMapping("/test")
    public ResponseEntity<String> test(@RequestParam String provider, @RequestParam String model) {
        aiService.sendTicketToAiOrchestrator(new InTicketVO(9L, "asd", "asd", "openai", "gpt-4o"));
        return new ResponseEntity<>("autenticado user", HttpStatus.OK);
    }

    @GetMapping("/test2")
    public ResponseEntity<String> test2(@RequestBody InTicketVO ticketVO) {
        aiService.sendTicketToAiOrchestrator(ticketVO);
        return new ResponseEntity<>("autenticado user", HttpStatus.OK);
    }

    @GetMapping("/test3")
    public ResponseEntity<String> test3(@RequestBody InReviewVO inReviewVO) {
        aiService.sendReviewToAiOrchestrator(inReviewVO);
        return new ResponseEntity<>("autenticado user", HttpStatus.OK);
    }
}
