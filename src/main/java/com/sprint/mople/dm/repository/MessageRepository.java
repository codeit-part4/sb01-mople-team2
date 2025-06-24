package com.sprint.mople.dm.repository;

import com.sprint.mople.dm.entity.Message;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, UUID> {

}
