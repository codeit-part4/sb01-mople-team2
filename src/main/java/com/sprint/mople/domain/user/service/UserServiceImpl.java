package com.sprint.mople.domain.user.service;

import com.sprint.mople.domain.user.dto.UpdateRoleResponse;
import com.sprint.mople.domain.user.dto.UserListResponse;
import com.sprint.mople.domain.user.dto.UserLoginResponse;
import com.sprint.mople.domain.user.dto.UserRegisterRequest;
import com.sprint.mople.domain.user.dto.UserRegisterResponse;
import com.sprint.mople.domain.user.entity.Role;
import com.sprint.mople.domain.user.entity.User;
import com.sprint.mople.domain.user.entity.UserSource;
import com.sprint.mople.domain.user.exception.AccountLockedException;
import com.sprint.mople.domain.user.exception.EmailAlreadyExistsException;
import com.sprint.mople.domain.user.exception.LoginFailedException;
import com.sprint.mople.domain.user.repository.UserRepository;
import com.sprint.mople.global.jwt.JwtProvider;
import jakarta.servlet.http.HttpServletResponse;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final TokenServiceImpl tokenService;
  private final JwtProvider jwtProvider;

  @Override
  public UserRegisterResponse registerUser(UserRegisterRequest request) {
    if (userRepository.existsByEmail(request.email())) {
      throw new EmailAlreadyExistsException("이미 존재하는 이메일입니다.");
    }

    User user = new User();
    user.setUserName(request.name());
    user.setEmail(request.email());

    //비밀번호 BCrypt 암호화
    String encoded = passwordEncoder.encode(request.password());

    //유저 기본값
    user.setPassword(encoded);
    user.setUserSource(UserSource.LOCAL);
    user.setRole(Role.USER);
    user.setIsLocked(Boolean.FALSE);
    user.setIsUsingTempPassword(Boolean.FALSE);

    User saved = userRepository.save(user);

    return new UserRegisterResponse(saved.getId(), saved.getUserName(), saved.getEmail());
  }

  @Override
  public UserLoginResponse login(String email, String password, HttpServletResponse response) {
    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new LoginFailedException("이메일 또는 비밀번호가 일치하지 않습니다."));

    if (!passwordEncoder.matches(password, user.getPassword())) {
      throw new LoginFailedException("이메일 또는 비밀번호가 일치하지 않습니다.");
    }

    if (user.getIsLocked()) {
      throw new AccountLockedException("이 계정은 잠긴 계정입니다.");
    }

    // ✅ userId & email 기반으로 토큰 생성
    Map<String, String> tokens = tokenService.generateTokens(user.getId(), user.getEmail());
    String accessToken = tokens.get("accessToken");
    String refreshToken = tokens.get("refreshToken");

    // ✅ refreshToken을 HttpOnly 쿠키로 설정
    ResponseCookie cookie = ResponseCookie.from("refresh_token", refreshToken)
        .httpOnly(true)
        .secure(true)
        .path("/api/auth/refresh")
        .sameSite("Strict")
        .maxAge(jwtProvider.getRefreshExpirationSeconds())
        .build();
    response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

    return new UserLoginResponse(
        accessToken,
        "Bearer",
        jwtProvider.getAccessExpirationSeconds(),
        user.getId(),
        user.getEmail(),
        user.getUserName()
    );

  }

  @Override
  public Page<UserListResponse> getUsers(String search, Pageable pageable) {
    Specification<User> spec = getUserSearchSpec(search);

    return userRepository.findAll(spec, pageable)
        .map(user -> new UserListResponse(
            user.getUserName(),
            user.getEmail(),
            user.getIsLocked(),
            user.getCreateAt()
        ));
  }

  private Specification<User> getUserSearchSpec(String search) {
    return (root, query, cb) -> {
      if (search == null || search.trim().isEmpty()) {
        return cb.conjunction();
      }

      String like = "%" + search.toLowerCase() + "%";
      return cb.or(
          cb.like(cb.lower(root.get("userName")), like),
          cb.like(cb.lower(root.get("email")), like)
      );
    };
  }

  @Override
  public UpdateRoleResponse updateUserRole(UUID userId, Role newRole) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new RuntimeException("User not found"));

    user.setRole(newRole);
    user.setUpdateAt(Instant.now());

    userRepository.save(user);

    return new UpdateRoleResponse(
        user.getId(),
        user.getCreateAt(),
        user.getEmail(),
        user.getUserName(),
        user.getRole(),
        user.getUserSource(),
        Boolean.TRUE.equals(user.getIsLocked())
    );
  }

  @Override
  @Transactional
  public void updateUserPassword(UUID userId, String newPassword) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new RuntimeException("User not found"));

    //비밀번호 암호화
    String encoded = passwordEncoder.encode(newPassword);

    user.setPassword(encoded);
    user.setUpdateAt(Instant.now());

    userRepository.save(user);
  }

  @Override
  public UUID updateUserLockStatus(UUID userId, boolean isLocked) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new RuntimeException("User not found"));

    user.setIsLocked(isLocked);
    user.setUpdateAt(Instant.now());

    userRepository.save(user);

    return user.getId();
  }
}

