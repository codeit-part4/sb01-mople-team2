package com.sprint.mople.user;
import com.sprint.mople.domain.user.dto.UserRegisterRequest;
import com.sprint.mople.domain.user.dto.UserRegisterResponse;
import com.sprint.mople.domain.user.entity.User;
import com.sprint.mople.domain.user.exception.EmailAlreadyExistsException;
import com.sprint.mople.domain.user.repository.UserRepository;
import com.sprint.mople.domain.user.service.UserServiceImpl;
import jakarta.validation.ConstraintViolation;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import static org.junit.jupiter.api.Assertions.*;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class UserRegisterTest {

  @InjectMocks
  private UserServiceImpl userService;

  @Mock
  private UserRepository userRepository;
  @Mock
  private PasswordEncoder passwordEncoder;

  private final LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();


  @BeforeEach
  void setUp() {
    validator.afterPropertiesSet();
  }

  @Test
  void 회원가입_성공() {
    // given
    UserRegisterRequest request = new UserRegisterRequest("모두의 플리", "modu@gmail.com", "password123");

    when(userRepository.save(any(User.class)))
        .thenAnswer(invocation -> {
          User user = invocation.getArgument(0);
          user.setId(UUID.randomUUID());
          return user;
        });

    // when
    UserRegisterResponse response = userService.registerUser(request);

    // then
    assertNotNull(response.id());
    assertEquals("모두의 플리", response.name());
    assertEquals("modu@gmail.com", response.email());
  }

  @Test
  void 이메일_중복예외() {
    // given
    UserRegisterRequest request = new UserRegisterRequest("모두의 플리", "modu@gmail.com", "password123");

    // 이미 이메일이 존재한다고 가정
    when(userRepository.existsByEmail("modu@gmail.com")).thenReturn(true);

    // when & then
    EmailAlreadyExistsException exception = assertThrows(EmailAlreadyExistsException.class, () -> {
      userService.registerUser(request);
    });

    assertEquals("이미 존재하는 이메일입니다.", exception.getMessage());
  }

  @Test
  void 이메일형식_실패체크() {
    // given
    UserRegisterRequest invalidRequest = new UserRegisterRequest(
        "모두의 플리",
        "invalid-email",
        "password123"
    );


    // when
    Set<ConstraintViolation<UserRegisterRequest>> violations = validator.validate(invalidRequest);

    // then
    assertFalse(violations.isEmpty());

    boolean hasEmailError = violations.stream()
        .anyMatch(v -> v.getPropertyPath().toString().equals("email") &&
            v.getMessage().contains("올바른 이메일 형식"));

    assertTrue(hasEmailError);
  }
}
