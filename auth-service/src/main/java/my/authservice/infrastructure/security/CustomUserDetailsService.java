package my.authservice.infrastructure.security;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import my.authservice.application.ports.incoming.UserService;
import my.authservice.domain.model.entities.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

  private final UserService userService;

  @Override
  @Transactional(readOnly = true)
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

    User user = userService.getUserByUsername(username);

    List<GrantedAuthority> authoritiesList =
        user.getRoles().stream()
            .map(role -> new SimpleGrantedAuthority(role.getName()))
            .collect(Collectors.toUnmodifiableList());

    if (authoritiesList.isEmpty()) {
      throw new UsernameNotFoundException(
          "LoginServiceImpl.loadUserByUsername(): Error: User with username "
              + username
              + " does not have roles.");
    }

    return new org.springframework.security.core.userdetails.User(
        user.getUsername(), user.getPassword(), user.isActive(), true, true, true, authoritiesList);
  }
}
