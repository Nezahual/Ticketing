package my.authservice.interfaces.rest.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.authservice.application.ports.incoming.UserService;
import my.authservice.domain.model.entities.User;
import my.authservice.interfaces.rest.user.mappers.UserMapper;
import my.ticketing.openapi.interfaces.rest.UsersApi;
import my.ticketing.openapi.interfaces.rest.dtos.UserRequest;
import my.ticketing.openapi.interfaces.rest.dtos.UserResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController implements UsersApi {

  private final UserService userService;
  private final UserMapper userMapper;

  @Override
  public ResponseEntity<UserResponse> registerUser(@Valid UserRequest userRequest) {

    User user = userMapper.fromRequest(userRequest);
    user = userService.createUser(user);
    return new ResponseEntity<>(userMapper.toResponse(user), HttpStatus.CREATED);
  }

  @GetMapping("/testUser")
  public ResponseEntity<String> testUser() {

    return new ResponseEntity<>("autenticado user", HttpStatus.OK);
  }

  @GetMapping("/testAdmin")
  public ResponseEntity<String> testAdmin() {

    return new ResponseEntity<>("autenticado admin", HttpStatus.OK);
  }
}
