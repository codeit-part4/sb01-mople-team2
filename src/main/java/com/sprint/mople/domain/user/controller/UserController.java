package com.sprint.mople.domain.user.controller;

import com.sprint.mople.domain.user.dto.PasswordUpdateRequestDto;
import com.sprint.mople.domain.user.dto.RoleUpdateRequestDto;
import com.sprint.mople.domain.user.dto.RoleUpdateResponseDto;
import com.sprint.mople.domain.user.dto.UpdateUserLockRequestDto;
import com.sprint.mople.domain.user.dto.UserListResponseDto;
import com.sprint.mople.domain.user.dto.UserRegisterRequestDto;
import com.sprint.mople.domain.user.dto.UserRegisterResponseDto;
import com.sprint.mople.domain.user.service.UserService;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
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

  @GetMapping
  public ResponseEntity<Page<UserListResponseDto>> getUsers(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size,
      @RequestParam(defaultValue = "userName") String sort,
      @RequestParam(defaultValue = "asc") String direction,
      @RequestParam(required = false) String search
  ) {
    Sort.Direction sortDirection = direction.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;

    Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sort));

    Page<UserListResponseDto> result = userService.getUsers(search, pageable);

    return ResponseEntity.ok(result);
  }

  @PatchMapping("/{userId}/role")
  public ResponseEntity<RoleUpdateResponseDto> updateUserRole(
      @PathVariable UUID userId,
      @RequestBody RoleUpdateRequestDto request
  ) {
    RoleUpdateResponseDto response = userService.updateUserRole(userId, request.getRole());
    return ResponseEntity.ok(response);
  }

  @PatchMapping("/{userId}/password")
  public ResponseEntity<Void> updateUserPassword(
      @PathVariable UUID userId,
      @RequestBody PasswordUpdateRequestDto request
  ) {
    userService.updateUserPassword(userId, request.getPassword());
    return ResponseEntity.noContent().build();
  }

  @PatchMapping("/{userId}/lock")
  public ResponseEntity<UUID> updateLockStatus(
      @PathVariable UUID userId,
      @RequestBody UpdateUserLockRequestDto request
  ) {
    UUID result = userService.updateUserLockStatus(userId, request.getIsLocked());
    return ResponseEntity.ok(result);
  }

}
