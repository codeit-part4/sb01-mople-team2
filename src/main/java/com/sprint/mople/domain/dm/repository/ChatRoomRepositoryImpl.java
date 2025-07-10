package com.sprint.mople.domain.dm.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sprint.mople.domain.dm.entity.ChatRoom;
import com.sprint.mople.domain.dm.entity.QChatRoomUser;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import com.sprint.mople.domain.dm.entity.QChatRoom;

@RequiredArgsConstructor
public class ChatRoomRepositoryImpl implements CustomChatRoomRepository {

  private final JPAQueryFactory queryFactory;

  @Override
  public Page<ChatRoom> findAllByUserIdWithParticipants(UUID userId, Pageable pageable) {
    QChatRoom cr = QChatRoom.chatRoom;
    QChatRoomUser cu = QChatRoomUser.chatRoomUser;

    List<ChatRoom> content = queryFactory
        .selectFrom(cr)
        .distinct()
        .join(cr.participants, cu).fetchJoin()
        .where(cu.user.id.eq(userId))
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .fetch();

    Long total = queryFactory
        .select(cr.countDistinct())
        .from(cr)
        .join(cr.participants, cu)
        .where(cu.user.id.eq(userId))
        .fetchOne();

    return new PageImpl<>(content, pageable, total != null ? total : 0);
  }
}
