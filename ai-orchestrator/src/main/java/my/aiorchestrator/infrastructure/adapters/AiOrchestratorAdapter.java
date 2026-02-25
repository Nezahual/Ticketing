package my.aiorchestrator.infrastructure.adapters;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.aiorchestrator.application.ports.outgoing.AiOrchestrator;
import my.aiorchestrator.application.ports.outgoing.ChromaService;
import my.aiorchestrator.configuration.AiModelsProperties;
import my.aiorchestrator.domain.model.vos.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.ai.converter.StructuredOutputConverter;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@Slf4j
@RequiredArgsConstructor
public class AiOrchestratorAdapter implements AiOrchestrator {

    private final Map<String, ChatModel> chatModels;
    private final ChromaService chromaService;
    private final AiModelsProperties aiModelsProperties;
    private final ChromaServiceAdapter chromaServiceAdapter;
    private final SimpleLoggerAdvisor simpleLoggerAdvisor;

    @Value("${ai.default-provider}")
    private String defaultProvider;

    @Value("${ai.default-model}")
    private String defaultModel;

    @Value("classpath:SystemPrompts/ReviewEvaluator.txt")
    private Resource reviewEvaluatorSystemPromptResource;

    @Value("classpath:SystemPrompts/QuestionPrompt.txt")
    private Resource questionSystemPromptResource;

    @Override
    public OutTicketVO sendTicketToLLM(InTicketVO ticketVO) {

        ChatModel model = this.loadModel(ticketVO.aiProvider(), ticketVO.aiModel());

        return null;
    }

    @Override
    public OutReviewVO categorizeReviewFeeling(InReviewVO inReviewVO) {

        ChatModel model = this.loadModel(defaultProvider, defaultModel);
        // ChatModel model = this.buildModel(aiModelsProperties.getConfig(defaultProvider,
        // defaultModel), defaultModel);

        StructuredOutputConverter<OutFeelingScoreVO> converter =
                new BeanOutputConverter<>(new ParameterizedTypeReference<>() {});
        ChatClient client = ChatClient.builder(model).defaultUser(inReviewVO.message()).build();

        ChatResponse rawResponse =
                client.prompt()
                        .system(
                                s ->
                                        s.text(reviewEvaluatorSystemPromptResource)
                                                .param("format", converter.getFormat()))
                        .user(inReviewVO.message())
                        .call()
                        .chatResponse();

        // TODO:
        /*if (rawResponse == null)*/
        // lanzar excepción para enviar mensaje con excepción

        return new OutReviewVO(
                inReviewVO.reviewId(),
                inReviewVO.userId(),
                converter.convert(rawResponse.getResult().getOutput().getText()),
                rawResponse.getMetadata().getUsage().getPromptTokens(),
                rawResponse.getMetadata().getUsage().getCompletionTokens(),
                rawResponse.getMetadata().getModel());
    }

    @Override
    public OutQuestionVO askQuestionToLLm(InQuestionVO inQuestionVO) {

        ChatModel model = this.loadModel(inQuestionVO.aiProvider(), inQuestionVO.aiModel());
        ChatClient client = ChatClient.builder(model).defaultUser(inQuestionVO.question()).defaultAdvisors(simpleLoggerAdvisor).build();
        ChatClient.ChatClientRequestSpec prompt = client.prompt();

        if (!StringUtils.isEmpty(inQuestionVO.fileName())) {
            Map<String, Object> metadata =
                    this.buildDocumentMetadata(
                            inQuestionVO.sessionId(),
                            inQuestionVO.user(),
                            inQuestionVO.documentId(),
                            inQuestionVO.fileName(),
                            inQuestionVO.uploadedAt());
            chromaServiceAdapter.ingestDocument(
                    inQuestionVO.fileName(), inQuestionVO.documentId(), metadata);
            PromptTemplate promptTemplate =
                    PromptTemplate.builder().resource(questionSystemPromptResource).build();
            QuestionAnswerAdvisor advisor =
                    chromaServiceAdapter.buildDocumentAdvisor(
                            promptTemplate, inQuestionVO.question());

            prompt.advisors(advisor);
        }

        ChatResponse rawResponse = prompt.call().chatResponse();
        String textContext = rawResponse.getResult().getOutput().getText();

        return null;
    }

    private OutQuestionVO askQuestionToLLm2(InQuestionVO inQuestionVO) {

        ChatModel model = this.loadModel(defaultProvider, defaultModel);
        QuestionAnswerAdvisor advisor;
        ChatClient client;

        if (!StringUtils.isEmpty(inQuestionVO.fileName())) {
            Map<String, Object> metadata =
                    this.buildDocumentMetadata(
                            inQuestionVO.sessionId(),
                            inQuestionVO.user(),
                            inQuestionVO.documentId(),
                            inQuestionVO.fileName(),
                            inQuestionVO.uploadedAt());
            chromaServiceAdapter.ingestDocument(
                    inQuestionVO.fileName(), inQuestionVO.documentId(), metadata);
            PromptTemplate prompt =
                    PromptTemplate.builder().resource(questionSystemPromptResource).build();
            advisor = chromaServiceAdapter.buildDocumentAdvisor(prompt, inQuestionVO.question());
            client = this.customChatClientBuilder(model, inQuestionVO.question(), List.of(advisor));
        } else client = this.customChatClientBuilder(model, "", null);

        client.prompt();

        return null;
    }

    private ChatClient customChatClientBuilder(
            ChatModel model, String systemString, List<Advisor> advisors) {

        if (CollectionUtils.isNotEmpty(advisors))
            return ChatClient.builder(model)
                    .defaultSystem(systemString)
                    .defaultAdvisors(advisors)
                    .build();
        else return ChatClient.builder(model).defaultSystem(systemString).build();
    }

    private Map<String, Object> buildDocumentMetadata(
            String sessionId,
            Long userId,
            String documentId,
            String filename,
            LocalDateTime timestamp) {

        return Map.of(
                "sessionId",
                sessionId,
                "userId",
                userId,
                "documentId",
                documentId,
                "filename",
                filename,
                "timestamp",
                timestamp);
    }

    private ChatModel loadModel(String provider, String model) {

        return this.chatModels.get(provider + ":" + model);
    }
}
