package com.sprint.mople.domain.content.service;

import com.sprint.mople.domain.content.dto.ContentMetadataResponse;
import com.sprint.mople.domain.content.dto.ContentSearchRequest;
import com.sprint.mople.domain.content.entity.Content;
import com.sprint.mople.domain.content.exception.ContentNotFoundException;
import com.sprint.mople.domain.content.repository.ContentRepository;
import com.sprint.mople.global.dto.Cursor;
import com.sprint.mople.global.dto.PageResponseDto;
import com.sprint.mople.global.util.CursorEncoder;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ContentServiceImpl implements ContentService {

  private final ContentRepository contentRepository;
  private final CursorEncoder cursorEncoder;

  @Override
  public ContentMetadataResponse getContentDetail(UUID contentId) {
    Content content = contentRepository.findById(contentId)
        .orElseThrow(ContentNotFoundException::new);
    return ContentMetadataResponse.from(content);
  }

  @Override
  public PageResponseDto<ContentMetadataResponse> getPaginatedContentList(
      ContentSearchRequest request) {
    String title = request.getTitle();
    int size = request.getSize();
    String cursor = request.getCursor();

    String lastValue = null;
    UUID lastId = null;

    if (cursor != null && !cursor.isEmpty()) {
      Cursor decoded = cursorEncoder.decode(cursor);
      lastValue = decoded.lastValue();
      if (decoded.lastId() != null) {
        lastId = UUID.fromString(decoded.lastId());
      }
    }

    List<Content> contents = contentRepository.findContentsWithCursor(
        title, lastValue, lastId, size + 1
    );

    boolean hasNext = contents.size() > size;
    if (hasNext) {
      contents = contents.subList(0, size);
    }

    List<ContentMetadataResponse> result = contents.stream()
        .map(ContentMetadataResponse::from)
        .toList();

    String nextCursor = null;
    if (hasNext) {
      Content last = contents.get(contents.size() - 1);
      nextCursor = cursorEncoder.encode(last.getTitle(), last.getId());
    }

    long totalElements = contentRepository.countContentsByTitle(title);

    return PageResponseDto.<ContentMetadataResponse>builder()
        .data(result)
        .nextCursor(nextCursor)
        .size(size)
        .totalElements(totalElements)
        .hasNext(hasNext)
        .build();
  }
}
