package com.sprint.mople.domain.user.controller;

import com.sprint.mople.domain.user.dto.UserRegisterRequestDto;
import com.sprint.mople.domain.user.dto.UserRegisterResponseDto;
import com.sprint.mople.domain.user.service.UserService;
import com.sprint.mople.domain.user.service.UserServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Validated
public class UserController {

  private final UserService userService;

  @PostMapping
  public ResponseEntity<UserRegisterResponseDto> registerUser(
      @Valid @RequestBody UserRegisterRequestDto request) {

    UserRegisterResponseDto response = userService.registerUser(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }
}
