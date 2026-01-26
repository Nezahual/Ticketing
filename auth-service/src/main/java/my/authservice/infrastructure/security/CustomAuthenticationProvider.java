package my.authservice.infrastructure.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

  private final CustomUserDetailsService customUserDetailsService;
  private final Argon2PasswordEncoder argon2PasswordEncoder;

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {

    String username = authentication.getName();
    Object credentials = authentication.getCredentials();

    if (!(credentials instanceof String)) return null;

    UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

    if (argon2PasswordEncoder.matches(credentials.toString(), userDetails.getPassword()))
      if (userDetails.isEnabled())
        if (userDetails.isAccountNonLocked())
          if (userDetails.isAccountNonExpired())
            return new UsernamePasswordAuthenticationToken(
                userDetails, userDetails.getPassword(), userDetails.getAuthorities());
          else throw new CredentialsExpiredException("Credentials expired for user: " + username);
        else throw new LockedException("Account is locked for user: " + username);
      else throw new DisabledException("Account is disabled for user: " + username);
    else throw new BadCredentialsException("Bad credentials for user: " + username);
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
  }
}
