package my.aiorchestrator.infrastructure.eventhandler.configuration;

import my.aiorchestrator.application.ports.outgoing.S3Service;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

    @Bean
    public TokenTextSplitter tokenTextSplitter() {

        return TokenTextSplitter.builder()
                .withChunkSize(500)
                .withKeepSeparator(true)
                .withMinChunkLengthToEmbed(10)
                .build();
    }
}
