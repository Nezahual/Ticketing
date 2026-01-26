package my.authservice.interfaces.rest.user.mappers;

import my.authservice.domain.model.entities.User;
import my.ticketing.openapi.interfaces.rest.dtos.UserRequest;
import my.ticketing.openapi.interfaces.rest.dtos.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

  @Mapping(target = "isActive", source = "active")
  UserResponse toResponse(User user);

  @Mapping(target = "active", source = "isActive")
  User fromRequest(UserRequest userRequest);
}
