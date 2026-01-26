package my.authservice.infrastructure.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.authentication.DefaultAuthenticationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CustomAuthenticationEventPublisher implements AuthenticationEventPublisher {

  private final DefaultAuthenticationEventPublisher delegate;

  public CustomAuthenticationEventPublisher(ApplicationEventPublisher publisher) {
    this.delegate = new DefaultAuthenticationEventPublisher(publisher);
  }

  @Override
  public void publishAuthenticationSuccess(Authentication authentication) {
    delegate.publishAuthenticationSuccess(authentication);
  }

  @Override
  public void publishAuthenticationFailure(
      AuthenticationException exception, Authentication authentication) {

    log.error("Fallo de autenticación OAuth2: {}", exception.getMessage(), exception);

    if (exception
        instanceof org.springframework.security.oauth2.core.OAuth2AuthenticationException oAuthEx) {
      log.error(
          "OAuth2 Error details - code: {}, description: {}, uri: {}",
          oAuthEx.getError().getErrorCode(),
          oAuthEx.getError().getDescription(),
          oAuthEx.getError().getUri());
    }

    delegate.publishAuthenticationFailure(exception, authentication);
  }
}
