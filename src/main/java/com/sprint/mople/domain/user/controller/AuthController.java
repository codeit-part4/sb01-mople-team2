package com.sprint.mople.domain.user.controller;

import com.sprint.mople.domain.user.dto.UserLoginRequestDto;
import com.sprint.mople.domain.user.dto.UserLoginResponseDto;
import com.sprint.mople.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

  private final UserService userService;

  @PostMapping("/login")
  public ResponseEntity<UserLoginResponseDto> login(@RequestBody @Valid UserLoginRequestDto request) {
    UserLoginResponseDto response = userService.login(request.getEmail(), request.getPassword());
    return ResponseEntity.ok(response);
  }
}

