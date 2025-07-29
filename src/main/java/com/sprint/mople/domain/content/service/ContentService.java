package com.sprint.mople.domain.content.service;

import com.sprint.mople.domain.content.dto.ContentMetadataResponse;
import java.util.List;
import java.util.UUID;

public interface ContentService {
  ContentMetadataResponse getContentById(UUID contentId);
  List<ContentMetadataResponse> getAllContents();
}