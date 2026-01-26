package my.authservice.infrastructure.persistence.jpa.entities;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class UserEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @Column(name = "username")
  private String username;

  @Column(name = "password")
  private String password;

  @Column(name = "mail")
  private String mail;

  @Column(name = "birth_date")
  private LocalDate birthDate;

  @Column(name = "active")
  private boolean active;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
      name = "users_role", // Corrected table name to match V3__Create_user_role_table.sql
      joinColumns = @JoinColumn(name = "user_id"),
      inverseJoinColumns = @JoinColumn(name = "role_id"))
  @Builder.Default
  private Set<RoleEntity> roles = new HashSet<>();
}
