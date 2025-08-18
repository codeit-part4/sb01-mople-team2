package com.sprint.mople.domain.content.repository;

import com.sprint.mople.domain.content.entity.Genre;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GenreRepository extends JpaRepository<Genre, Long> {

  // 장르 이름으로 엔티티를 찾는 메서드 추가
  Optional<Genre> findByName(String name);
}
