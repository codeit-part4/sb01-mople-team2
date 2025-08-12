package com.sprint.mople.domain.content.service;

import com.sprint.mople.domain.content.dto.ContentMetadataResponse;
import com.sprint.mople.domain.content.dto.ContentSearchRequest;
import com.sprint.mople.domain.content.entity.Content;
import com.sprint.mople.domain.content.exception.ContentNotFoundException;
import com.sprint.mople.domain.content.repository.ContentRepository;
import com.sprint.mople.domain.watchsession.entity.WatchSession;
import com.sprint.mople.domain.watchsession.repository.WatchSessionParticipantRepository;
import com.sprint.mople.domain.watchsession.repository.WatchSessionRepository;
import com.sprint.mople.global.dto.Cursor;
import com.sprint.mople.global.dto.PageResponseDto;
import com.sprint.mople.global.util.CursorEncoder;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ContentServiceImpl implements ContentService {

  private final ContentRepository contentRepository;
  private final CursorEncoder cursorEncoder;

  private final WatchSessionRepository watchSessionRepository;
  private final WatchSessionParticipantRepository watchSessionParticipantRepository;

  @Override
  @Transactional(readOnly = true)
  public ContentMetadataResponse getContentDetail(UUID contentId) {
    Content content = contentRepository
        .findById(contentId)
        .orElseThrow(ContentNotFoundException::new);

    Optional<WatchSession> sessionOpt = watchSessionRepository.findByContentId(contentId);
    int viewerCount = sessionOpt
        .map(session -> watchSessionParticipantRepository.countBySessionId(session.getId()))
        .orElse(0);

    return ContentMetadataResponse.from(content, viewerCount);
  }

  @Override
  @Transactional(readOnly = true)
  public PageResponseDto<ContentMetadataResponse> getPaginatedContentList(
      ContentSearchRequest request
  )
  {
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

    PageRequest pageRequest = PageRequest.of(0, size+1);
    List<Content> contents = contentRepository.findContentsWithAllRelations(
        title, lastValue, lastId, pageRequest
    );

    boolean hasNext = contents.size() > size;
    if (hasNext) {
      contents = contents.subList(0, size);
    }

    List<ContentMetadataResponse> result = contents
        .stream()
        .map(content -> {
          int viewerCount = Optional.ofNullable(content.getWatchSession())
              .map(WatchSession::getParticipants)
              .map(Set::size)
              .orElse(0);

          return ContentMetadataResponse.from(content, viewerCount);
        })
        .toList();

    String nextCursor = null;
    if (hasNext) {
      Content last = contents.get(contents.size() - 1);
      nextCursor = cursorEncoder.encode(last.getNormalizedTitle(), last.getId());
    }

    long totalElements = contentRepository.countContentsByTitle(title);

    return PageResponseDto
        .<ContentMetadataResponse>builder()
        .data(result)
        .nextCursor(nextCursor)
        .size(size)
        .totalElements(totalElements)
        .hasNext(hasNext)
        .build();
  }
}
