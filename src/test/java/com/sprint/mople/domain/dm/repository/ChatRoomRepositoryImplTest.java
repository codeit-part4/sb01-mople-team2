package com.sprint.mople.domain.dm.repository;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import com.sprint.mople.domain.dm.entity.ChatRoom;
import com.sprint.mople.domain.user.entity.User;
import com.sprint.mople.domain.user.entity.UserSource;
import com.sprint.mople.domain.user.entity.Role;
import com.sprint.mople.domain.user.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class ChatRoomRepositoryImplTest {

  @Autowired
  EntityManager em;

  @Autowired
  UserRepository userRepository;

  @Autowired
  ChatRoomRepository chatRoomRepository;

  @Autowired
  ChatRoomUserRepository chatRoomUserRepository;

  @Autowired
  PasswordEncoder passwordEncoder;

  @Test
  void 특정_유저가_참가한_채팅방과_모든_참가자_조회() {
    // given
    User user1 = new User();
    user1.setUserName("user1");
    user1.setEmail("user1@example.com");
    user1.setPassword(passwordEncoder.encode("password1"));
    user1.setUserSource(UserSource.LOCAL);
    user1.setRole(Role.USER);
    user1.setIsLocked(false);
    user1.setIsUsingTempPassword(false);
    userRepository.save(user1);
    System.out.println("User1 saved: " + user1.getId());

    User user2 = new User();
    user2.setUserName("user2");
    user2.setEmail("user2@example.com");
    user2.setPassword(passwordEncoder.encode("password2"));
    user2.setUserSource(UserSource.LOCAL);
    user2.setRole(Role.USER);
    user2.setIsLocked(false);
    user2.setIsUsingTempPassword(false);
    userRepository.save(user2);
    System.out.println("User2 saved: " + user2.getId());

    User user3 = new User();
    user3.setUserName("user3");
    user3.setEmail("user3@example.com");
    user3.setPassword(passwordEncoder.encode("password3"));
    user3.setUserSource(UserSource.LOCAL);
    user3.setRole(Role.USER);
    user3.setIsLocked(false);
    user3.setIsUsingTempPassword(false);
    userRepository.save(user3);
    System.out.println("User3 saved: " + user3.getId());


    ChatRoom room1 = new ChatRoom(user1, user2);
    System.out.println("ChatRoom1 created with participants: " + user1.getId() + ", " + user2.getId());
    ChatRoom room2 = new ChatRoom(user1, user3);
    System.out.println("ChatRoom2 created with participants: " + user1.getId() + ", " + user3.getId());

    chatRoomRepository.save(room1);
    chatRoomRepository.save(room2);

    em.flush();
    em.clear();

    // when
    Page<ChatRoom> result = chatRoomRepository.findAllByUserIdWithParticipants(user1.getId(), PageRequest.of(0, 10));

    // then
    assertThat(result.getContent()).hasSize(2);

    for (ChatRoom chatRoom : result.getContent()) {
      assertThat(chatRoom.getParticipants()).hasSize(2);
    }
  }
}