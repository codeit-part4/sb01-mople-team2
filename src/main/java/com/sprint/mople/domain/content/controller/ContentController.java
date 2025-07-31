package com.sprint.mople.domain.content.controller;

import com.sprint.mople.domain.content.dto.ContentMetadataResponse;
import com.sprint.mople.domain.content.dto.ContentSearchRequest;
import com.sprint.mople.domain.content.service.ContentService;
import com.sprint.mople.global.dto.PageResponseDto;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/contents")
@RequiredArgsConstructor
public class ContentController {

  private final ContentService contentService;

  @GetMapping("/{contentId}")
  public ResponseEntity<ContentMetadataResponse> getContent(@PathVariable UUID contentId) {
    return ResponseEntity.ok(contentService.getContentDetail(contentId));
  }

  @GetMapping
  public ResponseEntity<PageResponseDto<ContentMetadataResponse>> getAll(
      @Valid @ParameterObject @ModelAttribute ContentSearchRequest request
  ) {
    return ResponseEntity.ok(contentService.getPaginatedContentList(request));
  }
}