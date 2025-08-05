package com.sprint.mople.domain.content.service;

import com.sprint.mople.domain.content.dto.ContentMetadataResponse;
import com.sprint.mople.domain.content.dto.ContentSearchRequest;
import com.sprint.mople.global.dto.PageResponseDto;
import java.util.List;
import java.util.UUID;

public interface ContentService {
  ContentMetadataResponse getContentDetail(UUID contentId);
  PageResponseDto<ContentMetadataResponse> getPaginatedContentList(ContentSearchRequest request);
}