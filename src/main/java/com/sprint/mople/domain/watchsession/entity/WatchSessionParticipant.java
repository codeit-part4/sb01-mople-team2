package com.sprint.mople.domain.watchsession.entity;

import com.sprint.mople.domain.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.Instant;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
    name = "watch_session_participants",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"session_id", "user_id"})
    }
)
@Getter
@NoArgsConstructor
public class WatchSessionParticipant {

  @Id
  @Column(name = "participant_id", columnDefinition = "uuid")
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "session_id", nullable = false)
  private WatchSession session;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @Column(name = "joined_at", columnDefinition = "timestamp with time zone", nullable = false)
  private Instant joinedAt;

  @PrePersist
  protected void onJoin() {
    this.joinedAt = Instant.now();
  }
}
