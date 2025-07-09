package com.sprint.mople.domain.user.controller;

import com.sprint.mople.domain.user.dto.ResetPasswordRequest;
import com.sprint.mople.domain.user.dto.UserLoginRequest;
import com.sprint.mople.domain.user.dto.UserLoginResponse;
import com.sprint.mople.domain.user.dto.UserProfileResponse;
import com.sprint.mople.domain.user.entity.User;
import com.sprint.mople.domain.user.exception.NotFoundException;
import com.sprint.mople.domain.user.exception.UnauthorizedException;
import com.sprint.mople.domain.user.repository.UserRepository;
import com.sprint.mople.domain.user.service.TokenService;
import com.sprint.mople.domain.user.service.UserService;
import com.sprint.mople.global.jwt.JwtProvider;
import com.sprint.mople.global.jwt.JwtTokenExtractor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

  private final UserService userService;
  private final TokenService tokenService;
  private final UserRepository userRepository;
  private final JwtProvider jwtProvider;


  @PostMapping("/login")
  public ResponseEntity<UserLoginResponse> login(
      @RequestBody @Valid UserLoginRequest request,
      HttpServletResponse response
  ) {
    return ResponseEntity.ok(userService.login(request.email(), request.password(), response));
  }

  @PostMapping("/refresh")
  public ResponseEntity<Map<String, String>> refreshToken(
      @CookieValue(value = "refresh_token", required = false) String refreshToken,
      HttpServletResponse response
  ) {
    if (refreshToken == null) {
      throw new UnauthorizedException("Refresh Token이 존재하지 않습니다.");
    }

    Map<String, String> newTokens = tokenService.reissueTokens(refreshToken, response);

    return ResponseEntity.ok(newTokens);
  }

  @PostMapping("/logout")
  public ResponseEntity<Void> logout(
      @CookieValue(value = "refresh_token", required = false) String refreshToken,
      HttpServletResponse response
  ) {
    if (refreshToken != null) {
      tokenService.logout(refreshToken, response);
    }

    return ResponseEntity.noContent().build(); // 204
  }

  @PostMapping("/reset-password")
  public ResponseEntity<Map<String, String>> resetPassword(
      @RequestBody @Valid ResetPasswordRequest request) {
    userService.resetPassword(request.email());

    return ResponseEntity.ok(Map.of(
        "message", "임시 비밀번호가 해당 이메일로 전송되었습니다."
    ));
  }

  @GetMapping("/me")
  public ResponseEntity<UserProfileResponse> getMe(HttpServletRequest request) {
    UUID userId = JwtTokenExtractor.extractUserId(request, jwtProvider);

    User user = userRepository.findById(userId)
        .orElseThrow(() -> new NotFoundException("사용자를 찾을 수 없습니다."));

    return ResponseEntity.ok(
        new UserProfileResponse(user.getId(), user.getEmail(), user.getUserName())
    );
  }
}

