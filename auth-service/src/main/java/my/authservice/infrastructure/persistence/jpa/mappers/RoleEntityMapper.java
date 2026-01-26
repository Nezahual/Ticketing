package my.authservice.infrastructure.persistence.jpa.mappers;

import my.authservice.domain.model.entities.Role;
import my.authservice.infrastructure.persistence.jpa.entities.RoleEntity;

public interface RoleEntityMapper {

  Role toDomain(RoleEntity entity);

  RoleEntity toEntity(Role domain);
}
