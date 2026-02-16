package my.aiorchestrator.interfaces.rest;

import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.aiorchestrator.application.ports.incoming.AiService;
import my.aiorchestrator.application.ports.outgoing.S3Service;
import my.aiorchestrator.domain.model.vos.InReviewVO;
import my.aiorchestrator.domain.model.vos.InTicketVO;
import my.aiorchestrator.domain.model.vos.InWeatherVO;
import my.aiorchestrator.domain.model.vos.OutReviewVO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@Slf4j
public class TestController {

    private final AiService aiService;
    private final S3Service s3Service;

    @GetMapping("/test")
    public ResponseEntity<String> test(@RequestParam String provider, @RequestParam String model) {
        aiService.sendTicketToAiOrchestrator(new InTicketVO(9L, "asd", "asd", "openai", "gpt-4o"));
        return new ResponseEntity<>("autenticado user", HttpStatus.OK);
    }

    @PostMapping("/test2")
    public ResponseEntity<String> test2(@RequestBody InTicketVO ticketVO) {
        aiService.sendTicketToAiOrchestrator(ticketVO);
        return new ResponseEntity<>("autenticado user", HttpStatus.OK);
    }

    @PostMapping("/test3")
    public ResponseEntity<OutReviewVO> test3(@RequestBody InReviewVO inReviewVO) {
        OutReviewVO outReviewVO = aiService.sendReviewToAiOrchestrator(inReviewVO);
        return new ResponseEntity<>(outReviewVO, HttpStatus.OK);
    }

    @PostMapping("/test4")
    public ResponseEntity<String> test4(@RequestPart("file") MultipartFile file)
            throws IOException {

        s3Service.pushMultipartFileToBucket(file, "weatherbucket");
        aiService.sendTravelFileToAiOrchestrator(new InWeatherVO(123L, file.getName()));

        return new ResponseEntity<>("", HttpStatus.OK);
    }
}
