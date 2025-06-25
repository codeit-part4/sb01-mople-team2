package com.sprint.mople.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.sprint.mople.domain.user.dto.UserLoginResponseDto;
import com.sprint.mople.domain.user.entity.User;
import com.sprint.mople.domain.user.repository.UserRepository;
import com.sprint.mople.domain.user.service.UserServiceImpl;
import com.sprint.mople.global.jwt.JwtProvider;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
public class UserLoginTest {

  @InjectMocks
  private UserServiceImpl userService;

  @Mock
  private UserRepository userRepository;

  @Mock
  private PasswordEncoder passwordEncoder;
  @Mock
  private JwtProvider jwtProvider;

  @Test
  void 로그인_성공_JWT_반환() {
    // given
    String email = "modu@gmail.com";
    String password = "password123";
    UUID userId = UUID.randomUUID();

    User user = User.builder()
        .id(userId)
        .userName("모두의 플리")
        .email(email)
        .password("encodedPassword")
        .build();

    when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
    when(passwordEncoder.matches(password, user.getPassword())).thenReturn(true);
    when(jwtProvider.createToken(userId.toString(), email)).thenReturn("mock-jwt-token");

    // when
    UserLoginResponseDto response = userService.login(email, password);

    // then
    assertEquals("mock-jwt-token", response.getAccessToken());
    assertEquals("Bearer", response.getTokenType());
    assertEquals(email, response.getEmail());
    assertEquals("모두의 플리", response.getName());
    assertEquals(userId, response.getUserId());
  }

  @Test
  void 이메일이_존재하지_않으면_예외발생() {
    // given
    String email = "notfound@gmail.com";
    when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

    // when & then
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
      userService.login(email, "anypass");
    });

    assertEquals("이메일 또는 비밀번호가 일치하지 않습니다.", exception.getMessage());
  }

  @Test
  void 비밀번호가_일치하지_않으면_예외발생() {
    // given
    String email = "modu@gmail.com";
    String rawPassword = "wrongpass";
    String encodedPassword = "$2a$10$ENCRYPTED...";

    User user = new User();
    user.setEmail(email);
    user.setPassword(encodedPassword);

    when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
    when(passwordEncoder.matches(rawPassword, encodedPassword)).thenReturn(false);

    // when & then
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
      userService.login(email, rawPassword);
    });

    assertEquals("이메일 또는 비밀번호가 일치하지 않습니다.", exception.getMessage());
  }
}

