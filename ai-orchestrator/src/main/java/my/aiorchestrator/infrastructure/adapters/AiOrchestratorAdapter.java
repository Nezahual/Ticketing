package my.aiorchestrator.infrastructure.adapters;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.aiorchestrator.application.ports.outgoing.AiOrchestrator;
import my.aiorchestrator.application.ports.outgoing.ChromaService;
import my.aiorchestrator.configuration.AiModelsProperties;
import my.aiorchestrator.domain.model.vos.*;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.ai.converter.StructuredOutputConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AiOrchestratorAdapter implements AiOrchestrator {

    private final Map<String, ChatModel> chatModels;
    private final ChromaService chromaService;
    private final AiModelsProperties aiModelsProperties;

    @Value("${ai.defaultProvider}")
    private String defaultProvider;

    @Value("${ai.defaultModel}")
    private String defaultModel;

    @Value("classpath:SystemPrompts/ReviewEvaluator.txt")
    private Resource reviewEvaluatorSystemPromptResource;

    @Override
    public OutTicketVO sendTicketToLLM(InTicketVO ticketVO) {

        ChatModel model = this.loadModel(ticketVO.aiProvider(), ticketVO.aiModel());

        return null;
    }

    @Override
    public OutReviewVO categorizeReviewFeeling(InReviewVO inReviewVO) {

        ChatModel model = this.loadModel(defaultProvider, defaultModel);
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

        return new OutReviewVO(
                inReviewVO.reviewId(),
                inReviewVO.userId(),
                converter.convert(rawResponse.getResult().getOutput().getText()),
                rawResponse.getMetadata().getUsage().getPromptTokens(),
                rawResponse.getMetadata().getUsage().getCompletionTokens(),
                rawResponse.getMetadata().getModel());
    }

    private ChatModel loadModel(String provider, String model) {

        return this.chatModels.get(provider + ":" + model);
    }
}
