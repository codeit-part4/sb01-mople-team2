package com.sprint.mople.user;
import com.sprint.mople.domain.user.dto.UserRegisterRequestDto;
import com.sprint.mople.domain.user.dto.UserRegisterResponseDto;
import com.sprint.mople.domain.user.entity.User;
import com.sprint.mople.domain.user.repository.UserRepository;
import com.sprint.mople.domain.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class UserRegisterTest {

  @InjectMocks
  private UserService userService;

  @Mock
  private UserRepository userRepository;

  @Test
  void 회원가입에_성공한다() {
    // given
    UserRegisterRequestDto request = new UserRegisterRequestDto("모두의 플리", "modu@gmail.com", "password123");

    when(userRepository.save(any(User.class)))
        .thenAnswer(invocation -> {
          User user = invocation.getArgument(0);
          user.setId(UUID.randomUUID());
          return user;
        });

    // when
    UserRegisterResponseDto response = userService.registerUser(request);

    // then
    assertNotNull(response.getId());
    assertEquals("모두의 플리", response.getName());
    assertEquals("modu@gmail.com", response.getEmail());
  }

  @Test
  void 이메일_중복예외() {
    // given
    UserRegisterRequestDto request = new UserRegisterRequestDto("모두의 플리", "modu@gmail.com", "password123");

    // 이미 이메일이 존재한다고 가정
    when(userRepository.existsByEmail("modu@gmail.com")).thenReturn(true);

    // when & then
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
      userService.registerUser(request);
    });

    assertEquals("이미 존재하는 이메일입니다.", exception.getMessage());
  }
}
