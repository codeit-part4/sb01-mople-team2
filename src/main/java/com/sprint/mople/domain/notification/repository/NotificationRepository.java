package com.sprint.mople.domain.notification.repository;

import com.sprint.mople.domain.notification.entity.Notification;
import com.sprint.mople.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;


public interface NotificationRepository extends JpaRepository<Notification, UUID> {


    Page<Notification> findByUserOrderByCreatedAtDesc(User user, Pageable pageable);

    long countByUserAndIsReadFalse(User user);
}