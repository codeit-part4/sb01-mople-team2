package com.sprint.mople.domain.follow.repository;

import com.sprint.mople.domain.follow.entity.Follow;
import com.sprint.mople.domain.user.entity.User;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FollowRepository extends JpaRepository<Follow, UUID>, CustomFollowRepository {

  Optional<Follow> findByFollowerIdAndFolloweeId(UUID followerId, UUID followeeId);


}
