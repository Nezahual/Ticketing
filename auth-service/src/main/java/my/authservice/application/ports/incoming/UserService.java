package my.authservice.application.ports.incoming;

import my.authservice.domain.model.entities.User;

public interface UserService {

  User getUserByUsername(String username);

  User getUserById(Long userId);

  boolean existsUserByUsernameAndPassword(String username, String password);

  User createUser(User user);
}
