package my.aiorchestrator.interfaces.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.aiorchestrator.application.ports.incoming.AiService;
import my.aiorchestrator.domain.model.vos.InTicketVO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class TestController {

    private final AiService aiService;

    @GetMapping("/test")
    public ResponseEntity<String> testUser(
            @RequestParam String provider, @RequestParam String model) {
        aiService.sendTicketToAiOrchestrator(new InTicketVO(9L, "asd", "asd", "openai", "gpt-4o"));
        return new ResponseEntity<>("autenticado user", HttpStatus.OK);
    }
}
