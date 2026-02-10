package my.aiorchestrator.infrastructure.adapters;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.aiorchestrator.application.ports.outgoing.AiOrchestrator;
import my.aiorchestrator.application.ports.outgoing.ChromaService;
import my.aiorchestrator.configuration.AiModelsConfig;
import my.aiorchestrator.domain.model.vos.InReviewVO;
import my.aiorchestrator.domain.model.vos.InTicketVO;
import my.aiorchestrator.domain.model.vos.OutReviewVO;
import my.aiorchestrator.domain.model.vos.OutTicketVO;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AiOrchestratorAdapter implements AiOrchestrator {

    private final ChromaService chromaService;
    private final AiModelsConfig aiModelsConfig;

    @Value("${ai.defaultProvider}")
    private String defaultProvider;

    @Value("${ai.defaultProvider}")
    private String defaultModel;

    @Override
    public OutTicketVO sendTicketToLLM(InTicketVO ticketVO) {

        AiModelsConfig.AiConfig aiConfig = aiModelsConfig.getConfig(ticketVO.aiProvider(), ticketVO.aiModel());
        OpenAiChatOptions aiChatOptions = this.buildModelOptions(aiConfig, defaultModel);
        OpenAiChatModel openAiChatModel = this.buildModel(aiConfig, aiChatOptions, ticketVO.aiModel());
        //build chatclient


        return null;
    }

    @Override
    public OutReviewVO categorizeReviewFeeling(InReviewVO reviewVO) {

        AiModelsConfig.AiConfig aiConfig = aiModelsConfig.getConfig(defaultProvider, defaultModel);
        OpenAiChatOptions aiChatOptions = this.buildModelOptions(aiConfig, defaultModel);
        OpenAiChatModel openAiChatModel = this.buildModel(aiConfig, aiChatOptions, aiConfig.apiKEy());
        

        return null;
    }

    private OpenAiChatOptions buildModelOptions(AiModelsConfig.AiConfig aiConfig, String modelName) {

        return OpenAiChatOptions.builder()
                .model(modelName)
                .temperature(aiConfig.temperature())
                .maxTokens(aiConfig.maxTokens())
                .build();
    }

    private OpenAiChatModel buildModel(AiModelsConfig.AiConfig aiConfig, OpenAiChatOptions aiOptions, String modelName) {

        return OpenAiChatModel.builder()
                .openAiApi(OpenAiApi.builder().baseUrl(aiConfig.baseUrl()).apiKey(aiConfig.apiKEy()).build())
                .defaultOptions(aiOptions)
                .build();
    }
}
