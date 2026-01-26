package my.authservice.application.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.authservice.application.ports.incoming.UserService;
import my.authservice.domain.exceptions.NotFoundException;
import my.authservice.domain.model.entities.User;
import my.authservice.infrastructure.adapters.UserRepositoryAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class DefaultUserService implements UserService {

  private final UserRepositoryAdapter userRepositoryAdapter;
  private final PasswordEncoder passwordEncoder;

  @Override
  public User getUserByUsername(String username) {

    return userRepositoryAdapter
        .findUserByUsername(username)
        .orElseThrow(() -> new NotFoundException(User.class.getName(), "username", username));
  }

  @Override
  public User getUserById(Long userId) {

    return userRepositoryAdapter
        .findUserById(userId)
        .orElseThrow(() -> new NotFoundException(User.class.getName(), userId));
  }

  @Override
  public boolean existsUserByUsernameAndPassword(String username, String password) {

    String encodedPass = passwordEncoder.encode(password);

    return userRepositoryAdapter.existsUserByUsernameAndPassword(username, encodedPass);
  }

  @Override
  public User createUser(User user) {

    String encodedPass = passwordEncoder.encode(user.getPassword());

    user.setPassword(encodedPass);
    return userRepositoryAdapter.createUser(user);
  }
}
