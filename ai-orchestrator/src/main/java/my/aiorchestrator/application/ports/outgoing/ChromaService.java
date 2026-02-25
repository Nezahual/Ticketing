package my.aiorchestrator.application.ports.outgoing;

import java.util.List;
import java.util.Map;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;

public interface ChromaService {

    void ingestDocument(String filename, String documentId, Map<String, Object> metadata);

    QuestionAnswerAdvisor buildDocumentAdvisor(PromptTemplate promptTemplate, String question);

    List<Document> searchSimilar(String query);
}
