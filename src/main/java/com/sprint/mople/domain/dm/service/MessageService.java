package com.sprint.mople.domain.dm.service;

import com.sprint.mople.domain.dm.dto.MessageCreateRequest;
import com.sprint.mople.domain.dm.dto.MessageResponse;
import java.util.List;
import java.util.UUID;

public interface MessageService {

  MessageResponse create(MessageCreateRequest request);

  List<MessageResponse> findAll(UUID requestUserId, UUID targetUserId);

}
