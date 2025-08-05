package com.sprint.mople.domain.content.repository;

import com.sprint.mople.domain.content.entity.Content;
import java.util.List;
import java.util.UUID;

public interface ContentRepositoryCustom {

  List<Content> findContentsWithCursor(String title, String cursorValue, UUID cursorId, int size);
  long countContentsByTitle(String title);

}
