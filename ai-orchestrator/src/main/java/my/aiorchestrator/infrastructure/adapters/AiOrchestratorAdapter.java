package my.aiorchestrator.infrastructure.adapters;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import io.awspring.cloud.s3.S3Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.aiorchestrator.application.ports.outgoing.AiOrchestrator;
import my.aiorchestrator.application.ports.outgoing.ChromaService;
import my.aiorchestrator.application.ports.outgoing.S3Service;
import my.aiorchestrator.configuration.AiModelsProperties;
import my.aiorchestrator.domain.model.vos.*;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.ai.converter.StructuredOutputConverter;
import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
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
    private final S3Service s3Service;
    private final TokenTextSplitter tokenTextSplitter;

    @Value("${ai.default-provider}")
    private String defaultProvider;

    @Value("${ai.default-model}")
    private String defaultModel;

    @Value("${s3.weather-bucket}")
    private String weatherBucket;

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


        //TODO:
        /*if (rawResponse == null)*/
            //lanzar excepción para enviar mensaje con excepción

        return new OutReviewVO(
                inReviewVO.reviewId(),
                inReviewVO.userId(),
                converter.convert(rawResponse.getResult().getOutput().getText()),
                rawResponse.getMetadata().getUsage().getPromptTokens(),
                rawResponse.getMetadata().getUsage().getCompletionTokens(),
                rawResponse.getMetadata().getModel());
    }

    @Override
    public OutWeatherVO getWeatherForTravel(InWeatherVO inWeatherVO) throws IOException {

        S3Resource s3Resource = s3Service.downloadFromBucket(weatherBucket, inWeatherVO.fileName());
        String fullFile = s3Resource.getContentAsString(StandardCharsets.UTF_8);
        s3Service.deleteFileFromBucket(weatherBucket, inWeatherVO.fileName());
        //List<String> splittedFile = tokenTextSplitter.split(new Document(fullFile));
        return null;
    }

    public void getWeather(){
        
    }

    private ChatModel loadModel(String provider, String model) {

        return this.chatModels.get(provider + ":" + model);
    }
}
