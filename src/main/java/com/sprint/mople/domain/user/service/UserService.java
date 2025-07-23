package com.sprint.mople.domain.user.service;

import com.sprint.mople.domain.user.dto.UpdateRoleResponse;
import com.sprint.mople.domain.user.dto.UserListResponse;
import com.sprint.mople.domain.user.dto.UserLoginResponse;
import com.sprint.mople.domain.user.dto.UserRegisterRequest;
import com.sprint.mople.domain.user.dto.UserRegisterResponse;
import com.sprint.mople.domain.user.entity.Role;
import jakarta.servlet.http.HttpServletResponse;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {

  UserRegisterResponse registerUser(UserRegisterRequest request);

  UserLoginResponse login(String email, String rawPassword, HttpServletResponse response);

  Page<UserListResponse> getUsers(String search, Pageable pageable);

  UpdateRoleResponse updateUserRole(UUID userId, Role newRole);

  void updateUserPassword(UUID userId, String newPassword);

  UUID updateUserLockStatus(UUID userId, boolean locked);

  void resetPassword(String email);
}
