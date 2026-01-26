package my.authservice.domain.model.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@AllArgsConstructor
@Data
public class Role {

  private Long id;
  private String name;
  private String description;
}
