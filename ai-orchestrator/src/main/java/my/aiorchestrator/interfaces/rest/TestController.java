package my.aiorchestrator.interfaces.rest;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.aiorchestrator.application.ports.incoming.AiService;
import my.aiorchestrator.application.ports.outgoing.S3Service;
import my.aiorchestrator.domain.model.vos.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@Slf4j
public class TestController {

    @Value("${s3.questions-bucket}")
    private String questionsBucket;

    private final AiService aiService;
    private final S3Service s3Service;

    @GetMapping("/test")
    public ResponseEntity<String> test(@RequestParam String provider, @RequestParam String model) {
        aiService.sendTicketToAiOrchestrator(new InTicketVO(9L, "asd", "asd", "openai", "gpt-4o"));
        return new ResponseEntity<>("autenticado userId", HttpStatus.OK);
    }

    @PostMapping("/test2")
    public ResponseEntity<String> test2(@RequestBody InTicketVO ticketVO) {
        aiService.sendTicketToAiOrchestrator(ticketVO);
        return new ResponseEntity<>("autenticado userId", HttpStatus.OK);
    }

    @PostMapping("/test3")
    public ResponseEntity<OutReviewVO> test3(@RequestBody InReviewVO inReviewVO) {
        OutReviewVO outReviewVO = aiService.sendReviewToAiOrchestrator(inReviewVO);
        return new ResponseEntity<>(outReviewVO, HttpStatus.OK);
    }

    @PostMapping("/test4")
    public ResponseEntity<String> test4(
            @RequestPart(value = "file", required = false) MultipartFile file,
            @RequestPart("question") String question,
            @RequestPart("aiProvider") String aiProvider,
            @RequestPart("aiModel") String aiModel,
            @RequestPart("sessionId") String sessionId,
            @RequestPart("documentId") String documentId)
            throws IOException {

        this.fullService(file, question, aiProvider, aiModel, sessionId, documentId);
        return new ResponseEntity<>("", HttpStatus.OK);
    }

    private OutQuestionVO fullService(
            MultipartFile file,
            String question,
            String aiProvider,
            String aiModel,
            String sessionId,
            String documentId)
            throws IOException {

        // Todo esto irá en el ticket-service
        if (StringUtils.isEmpty(sessionId)) sessionId = UUID.randomUUID().toString();

        if (StringUtils.isEmpty(documentId)) documentId = UUID.randomUUID().toString();

        String originalFilename = file != null ? file.getOriginalFilename() : "";

        String s3Key = String.format("temp/%s/%s-%s", sessionId, documentId, originalFilename);

        if (file != null) s3Service.pushMultipartFileToBucket(file, questionsBucket, s3Key);

        InQuestionVO inQuestionVO =
                new InQuestionVO(
                        123L,
                        s3Key,
                        originalFilename,
                        question,
                        sessionId,
                        documentId,
                        LocalDateTime.now(),
                        aiProvider,
                        aiModel);
        return aiService.askAQuestion(inQuestionVO);
    }
}
