package my.aiorchestrator.infrastructure.adapters;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.aiorchestrator.application.ports.outgoing.ChromaService;
import my.aiorchestrator.application.ports.outgoing.S3Service;
import org.apache.commons.lang3.StringUtils;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.chroma.vectorstore.ChromaApi;
import org.springframework.ai.chroma.vectorstore.ChromaFilterExpressionConverter;
import org.springframework.ai.document.Document;
import org.springframework.ai.document.DocumentReader;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.filter.Filter;
import org.springframework.ai.vectorstore.filter.FilterExpressionBuilder;
import org.springframework.ai.vectorstore.filter.FilterExpressionTextParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChromaServiceAdapter implements ChromaService {

    private final S3Service s3Service;
    private final TokenTextSplitter tokenTextSplitter;
    private final VectorStore vectorStore;
    private final ChromaApi chromaApi;

    @Value("${s3.questions-bucket}")
    private String questionsBucket;

    @Value("${spring.ai.vectorstore.chroma.tenant-name}")
    private String tenantName;

    @Value("${spring.ai.vectorstore.chroma.database-name}")
    private String databaseName;

    @Value("${spring.ai.vectorstore.chroma.collection-name}")
    private String collectionName;

    @Override
    public String ingestDocument(
            String filename,
            String originalFilename,
            Long userId,
            LocalDateTime uploadedAt) {

        Resource s3Resource = s3Service.downloadFromBucket(questionsBucket, filename);
        String documentId = this.buildDocumentIdHash(s3Resource);

        if (!this.documentAlreadyExists(documentId)) {
            DocumentReader documentReader = this.getReader(filename, s3Resource);
            List<Document> docs = documentReader.read();

            HashMap<String, Object> metadata =
                    this.buildDocumentMetadata(
                            filename,
                            originalFilename,
                            userId,
                            documentId,
                            uploadedAt,
                            docs.size());

            List<Document> chunksWithMetadata = this.getChunksWithMetadata(docs, metadata);
            vectorStore.add(chunksWithMetadata);
        }

        return documentId;
    }

    private List<Document> getChunksWithMetadata(
            List<Document> docs, HashMap<String, Object> metadata) {

        AtomicInteger counter = new AtomicInteger(0);

        return docs.stream()
                .flatMap(
                        pageDoc -> {
                            Integer pageNumber = (Integer) pageDoc.getMetadata().get("page_number");
                            List<Document> pageChunks = tokenTextSplitter.apply(List.of(pageDoc));

                            pageChunks.forEach(
                                    chunk -> {
                                        metadata.put("page_number", pageNumber);
                                        metadata.put("chunk_index", counter.getAndIncrement());
                                    });

                            return pageChunks.stream()
                                    .map(
                                            chunk -> {
                                                String text = "";
                                                if (StringUtils.isNotBlank(chunk.getText()))
                                                    text = chunk.getText();
                                                return new Document(chunk.getId(), text, metadata);
                                            });
                        })
                .toList();
    }

    @Override
    public QuestionAnswerAdvisor buildDocumentAdvisor(
            PromptTemplate promptTemplate, String question, String documentId) {

        FilterExpressionBuilder filterBuilder = new FilterExpressionBuilder();
        Filter.Expression filter = filterBuilder.eq("document_id", documentId).build();

        SearchRequest search =
                SearchRequest.builder()
                        .query(question)
                        .similarityThreshold(0.2)
                        .topK(6)
                        .filterExpression(filter)
                        .build();

        return QuestionAnswerAdvisor.builder(vectorStore)
                .promptTemplate(promptTemplate)
                .searchRequest(search)
                .build();
    }

    @Override
    public List<Document> searchSimilar(String query) {

        return vectorStore.similaritySearch(query);
    }

    private HashMap<String, Object> buildDocumentMetadata(
            String filename,
            String originalFilename,
            Long userId,
            String documentId,
            LocalDateTime uploadedAt,
            int totalChunks) {

        return new HashMap<>(
                Map.of(
                        "filename", filename,
                        "original_filename", originalFilename,
                        "user_id", userId,
                        "document_id", documentId,
                        "uploaded_at", uploadedAt,
                        "total_chunks", totalChunks));
    }

    private boolean documentAlreadyExists(String docHash) {

        ChromaApi.GetEmbeddingsRequest request =
                new ChromaApi.GetEmbeddingsRequest(
                        Collections.emptyList(), chromaApi.where("{\"document_id\": {\"$eq\": \"" + docHash + "\"}}"));

        ChromaApi.Collection collection =
                chromaApi.getCollection(tenantName, databaseName, collectionName);

        if (collection == null)
            throw new IllegalArgumentException(
                    "Chroma vectorstore does not have collection named: " + collectionName);

        ChromaApi.GetEmbeddingResponse response =
                chromaApi.getEmbeddings(tenantName, databaseName, collection.id(), request);

        return !(response == null || response.metadata().isEmpty());
    }

    private String buildDocumentIdHash(Resource resource) {

        try {
            return "doc" + Arrays.toString(DigestUtils.md5Digest(resource.getInputStream()));
        } catch (IOException e) {
            return "doc" + UUID.randomUUID();
        }
    }

    private DocumentReader getReader(String filename, Resource resource) {

        if (filename.endsWith("pdf")) return new PagePdfDocumentReader(resource);
        else if (filename.endsWith("txt")) return new TextReader(resource);

        throw new UnsupportedOperationException("Formato no soportado: " + filename);
    }
}
