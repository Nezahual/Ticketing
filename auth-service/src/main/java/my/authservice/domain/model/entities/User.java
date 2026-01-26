package my.authservice.domain.model.entities;

import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@AllArgsConstructor
@Data
public class User {

  private Long id;
  private String username;
  private String password;
  private String mail;
  private String birthDate;
  private boolean active;
  private Set<Role> roles;
}
