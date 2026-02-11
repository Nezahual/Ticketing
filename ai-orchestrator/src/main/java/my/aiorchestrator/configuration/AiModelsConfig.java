package my.aiorchestrator.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class AiModelsConfig {

    private final AiModelsProperties aiModelsProperties;

    @Bean("openai:gpt-4o-mini")
    public ChatModel openaiGpt4oMini() {
        return this.getModel("openai", "gpt-4o-mini");
    }

    @Bean("openai:gpt-4o")
    public ChatModel openaiGpt4o() {
        return this.getModel("openai", "gpt-4o");
    }

    @Bean("groq:llama-3.1-70b")
    public ChatModel groqLlama3170b() {
        return this.getModel("groq", "llama-3.1-70b");
    }

    @Bean("groq:gemma-2-27b")
    public ChatModel groqGemma227b() {
        return this.getModel("groq", "gemma-2-27b");
    }

    @Bean("groq:llama-3.3-70b-versatile")
    public ChatModel groqLlama3370BVersatile() {
        return this.getModel("groq", "llama-3.3-70b-versatile");
    }

    @Bean("deepseek:deepseek-chat")
    public ChatModel deepseekDeepseekChat() {
        return this.getModel("deepseek", "deepseek-chat");
    }

    private ChatModel getModel(String provider, String model) {

        AiModelsProperties.AiConfig aiConfig = aiModelsProperties.getConfig(provider, model);
        OpenAiChatOptions aiOptions = this.buildModelOptions(aiConfig, model);

        return buildModel(aiConfig, aiOptions);
    }

    private OpenAiChatOptions buildModelOptions(
            AiModelsProperties.AiConfig aiConfig, String modelName) {

        return OpenAiChatOptions.builder()
                .model(modelName)
                .temperature(aiConfig.temperature())
                .maxTokens(aiConfig.maxTokens())
                .build();
    }

    private ChatModel buildModel(
            AiModelsProperties.AiConfig aiConfig, OpenAiChatOptions aiOptions) {

        return OpenAiChatModel.builder()
                .openAiApi(
                        OpenAiApi.builder()
                                .baseUrl(aiConfig.baseUrl())
                                .apiKey(aiConfig.apiKEy())
                                .build())
                .defaultOptions(aiOptions)
                .build();
    }
}
