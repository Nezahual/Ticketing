package my.authservice.infrastructure.adapters;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.authservice.application.ports.outgoing.UserRepository;
import my.authservice.domain.model.entities.User;
import my.authservice.infrastructure.persistence.jpa.entities.UserEntity;
import my.authservice.infrastructure.persistence.jpa.mappers.UserEntityMapper;
import my.authservice.infrastructure.persistence.repository.UserJpaRepository;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepository {

  private final UserEntityMapper userEntityMapper;
  private final UserJpaRepository userJpaRepository;

  @Override
  public Optional<User> findUserById(Long userId) {

    return userJpaRepository.findById(userId).map(userEntityMapper::toDomain);
  }

  @Override
  public boolean existsUserByUsernameAndPassword(String username, String password) {

    return userJpaRepository.existsByUsernameAndPassword(username, password);
  }

  @Override
  public User createUser(User user) {

    UserEntity userEntity = userEntityMapper.toEntity(user);
    userEntity = userJpaRepository.save(userEntity);
    return userEntityMapper.toDomain(userEntity);
  }

  @Override
  public Optional<User> findUserByUsername(String username) {

    return userJpaRepository.findByUsername(username).map(userEntityMapper::toDomain);
  }
}
