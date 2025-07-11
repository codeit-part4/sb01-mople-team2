package com.sprint.mople.domain.user.repository;

import com.sprint.mople.domain.user.entity.User;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, UUID>, JpaSpecificationExecutor<User> {

  boolean existsByEmail(String email);

  Optional<User> findByEmail(String email);

  Optional<User> findByUserName(String username);
}
