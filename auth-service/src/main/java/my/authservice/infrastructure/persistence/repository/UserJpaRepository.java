package my.authservice.infrastructure.persistence.repository;

import java.util.Optional;
import my.authservice.infrastructure.persistence.jpa.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {

  boolean existsByUsernameAndPassword(String username, String password);

  Optional<UserEntity> findByUsername(String username);
}
