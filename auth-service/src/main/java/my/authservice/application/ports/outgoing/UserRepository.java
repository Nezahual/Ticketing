package my.authservice.application.ports.outgoing;

import java.util.Optional;
import my.authservice.domain.model.entities.User;

public interface UserRepository {

  Optional<User> findUserById(Long userId);

  boolean existsUserByUsernameAndPassword(String username, String password);

  User createUser(User user);

  Optional<User> findUserByUsername(String username);
}
