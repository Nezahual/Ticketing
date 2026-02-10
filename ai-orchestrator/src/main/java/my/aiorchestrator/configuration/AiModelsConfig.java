package my.aiorchestrator.configuration;

import java.util.Map;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "ai")
public record AiModelsConfig(Map<String, AiProvider> providers) {

    public record AiProvider(String baseUrl, String apiKey, Map<String, AiModel> models) {

        public record AiModel(double temperature, int maxTokens) {}
    }

    public record AiConfig(String baseUrl, String apiKEy, double temperature, int maxTokens) {}

    public AiConfig getConfig(String provider, String model) {

        AiProvider aiProvider = providers.get(provider);

        if (aiProvider == null) {
            throw new IllegalArgumentException("Unknown provider: " + provider);
        }

        AiProvider.AiModel aiModel = aiProvider.models.get(model);

        if (aiModel == null) {
            throw new IllegalArgumentException("Unknown model: " + model);
        }

        return new AiConfig(
                aiProvider.baseUrl, aiProvider.apiKey, aiModel.temperature, aiModel.maxTokens);
    }
}
