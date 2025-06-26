package com.sprint.mople.domain.user.controller;

import com.sprint.mople.domain.user.dto.UserLoginRequest;
import com.sprint.mople.domain.user.dto.UserLoginResponse;
import com.sprint.mople.domain.user.service.TokenService;
import com.sprint.mople.domain.user.service.UserService;
import jakarta.validation.Valid;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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


  @PostMapping("/login")
  public ResponseEntity<UserLoginResponse> login(
      @RequestBody @Valid UserLoginRequest request) {
    UserLoginResponse response = userService.login(request.getEmail(), request.getPassword());
    return ResponseEntity.ok(response);
  }

  @PostMapping("/refresh")
  public ResponseEntity<?> refreshToken(@RequestBody Map<String, String> request) {
    String refreshToken = request.get("refreshToken");
    try {
      String newAccessToken = tokenService.refreshAccessToken(refreshToken);
      return ResponseEntity.ok(Map.of("accessToken", newAccessToken));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }
  }

}

