package my.aiorchestrator.application.ports.outgoing;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;

public interface ChromaService {

    String ingestDocument(
            String filename,
            String originalFilename,
            Long userId,
            LocalDateTime uploadedAt)
            throws IOException;

    QuestionAnswerAdvisor buildDocumentAdvisor(
            PromptTemplate promptTemplate, String question, String documentId);

    List<Document> searchSimilar(String query);
}
