package com.sprint.mople.domain.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor
public class User {

  @Id
  @Column(name = "user_id", columnDefinition = "uuid")
  @GeneratedValue
  @Setter
  private UUID id;

  @Column(name = "user_name", length = 25)
  @Setter
  private String userName;

  @Column(length = 50)
  @Setter
  private String email;

  @Column(length = 25)
  @Setter
  private String password;

  @Enumerated(EnumType.STRING)
  @Column(length = 25)
  @Setter
  private Role role;

  @Enumerated(EnumType.STRING)
  @Column(name = "user_source", length = 25)
  private UserSource userSource;

  @Column(name = "is_locked")
  @Setter
  private Boolean isLocked;

  @Column(name = "is_using_temp_password")
  @Setter
  private Boolean isUsingTempPassword;

  @CreationTimestamp
  @Column(name = "create_at", columnDefinition = "timestamp with time zone", updatable = false)
  private Instant createAt;

  @UpdateTimestamp
  @Column(name = "update_at", columnDefinition = "timestamp with time zone")
  @Setter
  private Instant updateAt;

}
