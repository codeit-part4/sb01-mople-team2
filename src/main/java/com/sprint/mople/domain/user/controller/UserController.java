package com.sprint.mople.domain.user.controller;


import com.sprint.mople.domain.user.dto.UpdatePasswordRequest;
import com.sprint.mople.domain.user.dto.UpdateRoleRequest;
import com.sprint.mople.domain.user.dto.UpdateRoleResponse;
import com.sprint.mople.domain.user.dto.UpdateUserLockRequest;
import com.sprint.mople.domain.user.dto.UserListResponse;
import com.sprint.mople.domain.user.dto.UserRegisterRequest;
import com.sprint.mople.domain.user.dto.UserRegisterResponse;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Validated
public class UserController {

  private final UserService userService;

  @PostMapping
  public ResponseEntity<UserRegisterResponse> registerUser(
      @Valid @RequestBody UserRegisterRequest request) {

    UserRegisterResponse response = userService.registerUser(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @GetMapping
  public ResponseEntity<Page<UserListResponse>> getUsers(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size,
      @RequestParam(defaultValue = "userName") String sort,
      @RequestParam(defaultValue = "asc") String direction,
      @RequestParam(required = false) String search
  ) {
    Sort.Direction sortDirection =
        direction.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;

    Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sort));

    Page<UserListResponse> result = userService.getUsers(search, pageable);

    return ResponseEntity.ok(result);
  }

  @PatchMapping("/{userId}/role")
  public ResponseEntity<UpdateRoleResponse> updateUserRole(
      @PathVariable UUID userId,
      @RequestBody UpdateRoleRequest request
  ) {
    UpdateRoleResponse response = userService.updateUserRole(userId, request.getRole());
    return ResponseEntity.ok(response);
  }

  @PatchMapping("/{userId}/password")
  public ResponseEntity<Void> updateUserPassword(
      @PathVariable UUID userId,
      @RequestBody UpdatePasswordRequest request
  ) {
    userService.updateUserPassword(userId, request.getPassword());
    return ResponseEntity.noContent().build();
  }

  @PatchMapping("/{userId}/lock")
  public ResponseEntity<UUID> updateLockStatus(
      @PathVariable UUID userId,
      @RequestBody UpdateUserLockRequest request
  ) {
    UUID result = userService.updateUserLockStatus(userId, request.getIsLocked());
    return ResponseEntity.ok(result);
  }

}
