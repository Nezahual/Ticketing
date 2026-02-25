package my.aiorchestrator.infrastructure.adapters;

import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.aiorchestrator.application.ports.outgoing.ChromaService;
import my.aiorchestrator.application.ports.outgoing.S3Service;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.document.DocumentReader;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChromaServiceAdapter implements ChromaService {

    private final S3Service s3Service;
    private final TokenTextSplitter tokenTextSplitter;
    private final VectorStore vectorStore;

    @Value("${s3.questions-bucket}")
    private String questionsBucket;

    @Override
    public void ingestDocument(String filename, String documentId, Map<String, Object> metadata) {

        Resource s3Resource = s3Service.downloadFromBucket(questionsBucket, filename);
        DocumentReader documentReader = this.getReader(filename, s3Resource);
        List<Document> docs = documentReader.read();
        docs = tokenTextSplitter.apply(docs);

        docs.forEach(d -> d.getMetadata().putAll(metadata));
        vectorStore.add(docs);
    }

    @Override
    public QuestionAnswerAdvisor buildDocumentAdvisor(
            PromptTemplate promptTemplate, String question) {

        SearchRequest search = SearchRequest.builder()
                .query(question)
                .similarityThreshold(0.7)
                .topK(6)
                .build();

        List<Document> docs = vectorStore.similaritySearch(search);

        return QuestionAnswerAdvisor.builder(vectorStore)
                .promptTemplate(promptTemplate)
                .searchRequest(
                        search)
                .build();
    }

    @Override
    public List<Document> searchSimilar(String query) {

        return vectorStore.similaritySearch(query);
    }

    private DocumentReader getReader(String filename, Resource resource) {

        if (filename.endsWith("pdf")) return new PagePdfDocumentReader(resource);
        else if (filename.endsWith("txt")) return new TextReader(resource);

        throw new UnsupportedOperationException("Formato no soportado: " + filename);
    }
}
