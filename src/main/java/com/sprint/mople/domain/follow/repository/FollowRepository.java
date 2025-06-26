package com.sprint.mople.domain.follow.repository;

import com.sprint.mople.domain.follow.entity.Follow;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowRepository extends JpaRepository<Follow, UUID> {

  Optional<Follow> findByFollowerIdAndFolloweeId(UUID followerId, UUID followeeId);
}
