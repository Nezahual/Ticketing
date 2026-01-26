package my.authservice.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.zalando.problem.jackson.ProblemModule;
import org.zalando.problem.violations.ConstraintViolationProblemModule;

@org.springframework.context.annotation.Configuration
@EnableAutoConfiguration(exclude = ErrorMvcAutoConfiguration.class)
public class ContextConfiguration {

  @Bean
  public ObjectMapper objectMapper() {
    return new ObjectMapper()
        .registerModules(new ProblemModule(), new ConstraintViolationProblemModule());
  }
}
