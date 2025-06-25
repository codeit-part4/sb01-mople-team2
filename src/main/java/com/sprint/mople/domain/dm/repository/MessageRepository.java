package com.sprint.mople.domain.dm.repository;

import com.sprint.mople.domain.dm.entity.Message;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, UUID> {

}
