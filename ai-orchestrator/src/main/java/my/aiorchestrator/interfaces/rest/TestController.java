package my.aiorchestrator.interfaces.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class TestController {

  @GetMapping("/users/testUser")
  public ResponseEntity<String> testUser() {

    return new ResponseEntity<>("autenticado user", HttpStatus.OK);
  }

  @GetMapping("/users/testAdmin")
  public ResponseEntity<String> testAdmin() {

    return new ResponseEntity<>("autenticado admin", HttpStatus.OK);
  }
}
