package com.sprint.mople.domain.content.service;

import com.sprint.mople.domain.content.dto.ContentMetadataResponse;
import com.sprint.mople.domain.content.entity.Content;
import com.sprint.mople.domain.content.exception.ContentNotFoundException;
import com.sprint.mople.domain.content.repository.ContentRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class ContentServiceImpl implements ContentService {

  private final ContentRepository contentRepository;

  @Override
  public ContentMetadataResponse getContentById(UUID contentId) {
    Content content = contentRepository.findById(contentId)
        .orElseThrow(ContentNotFoundException::new);

    return ContentMetadataResponse.from(content);
  }

  @Override
  public List<ContentMetadataResponse> getAllContents() {
    return contentRepository.findAll().stream()
        .map(ContentMetadataResponse::from)
        .toList();
  }
}
