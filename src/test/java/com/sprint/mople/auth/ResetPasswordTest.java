package com.sprint.mople.auth;

import com.sprint.mople.domain.user.entity.User;
import com.sprint.mople.domain.user.repository.UserRepository;
import com.sprint.mople.domain.user.service.EmailServiceImpl;
import com.sprint.mople.domain.user.service.UserServiceImpl;
import com.sprint.mople.global.util.TempPasswordUtil;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class ResetPasswordTest {

  @InjectMocks
  private UserServiceImpl userService;

  @Mock
  private UserRepository userRepository;

  @Mock
  private EmailServiceImpl emailService;

  @Mock
  private PasswordEncoder passwordEncoder;

  @Mock
  private TempPasswordUtil tempPasswordUtil;

  @Test
  void 비밀번호_재설정_요청_시_임시비밀번호를_저장하고_이메일을_보낸다() {
    // given
    String email = "modu@gmail.com";
    String rawTempPassword = "Temp1234!";
    String encodedTempPassword = "encoded";

    User user = User.builder()
        .id(UUID.randomUUID())
        .email(email)
        .userName("모두의 플리")
        .isLocked(false)
        .password("original")
        .build();

    when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
    when(tempPasswordUtil.generate()).thenReturn(rawTempPassword);
    when(passwordEncoder.encode(rawTempPassword)).thenReturn(encodedTempPassword);

    // when
    userService.resetPassword(email);

    // then
    verify(userRepository).save(argThat(savedUser ->
        savedUser.getPassword().equals(encodedTempPassword)
            && savedUser.getIsUsingTempPassword().equals(true)
    ));

    verify(emailService).sendTempPassword(email, rawTempPassword);
  }
}
