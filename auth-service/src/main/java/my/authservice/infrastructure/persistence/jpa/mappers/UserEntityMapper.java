package my.authservice.infrastructure.persistence.jpa.mappers;

import java.util.*;
import java.util.stream.Collectors;
import my.authservice.domain.model.entities.User;
import my.authservice.infrastructure.persistence.jpa.entities.RoleEntity;
import my.authservice.infrastructure.persistence.jpa.entities.UserEntity;
import org.apache.commons.collections4.CollectionUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface UserEntityMapper {

  // @Mapping(target = "roles", source = "roles", qualifiedByName = "fromSetLongToSetRole")
  UserEntity toEntity(User domain);

  // @Mapping(target = "roles", source = "roles", qualifiedByName = "fromSetRoleToSetLong")
  User toDomain(UserEntity entity);

  @Named("fromSetLongToSetRole")
  default Set<RoleEntity> fromSetLongToSetRole(Set<Long> roleIds) {

    return CollectionUtils.isNotEmpty(roleIds)
        ? roleIds.stream()
            .filter(Objects::nonNull)
            .map(r -> RoleEntity.builder().id(r).build())
            .collect(Collectors.toSet())
        : Collections.emptySet();
  }

  @Named("fromSetRoleToSetLong")
  default Set<Long> fromSetRoleToSetLong(Set<RoleEntity> roleEntitySet) {

    return CollectionUtils.isNotEmpty(roleEntitySet)
        ? roleEntitySet.stream()
            .filter(Objects::nonNull)
            .map(RoleEntity::getId)
            .collect(Collectors.toSet())
        : Collections.emptySet();
  }
}
