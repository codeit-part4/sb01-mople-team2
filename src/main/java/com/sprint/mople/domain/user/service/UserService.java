package com.sprint.mople.domain.user.service;

import com.sprint.mople.domain.user.dto.UserListResponseDto;
import com.sprint.mople.domain.user.dto.UserLoginResponseDto;
import com.sprint.mople.domain.user.dto.UserRegisterRequestDto;
import com.sprint.mople.domain.user.dto.UserRegisterResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {

  UserRegisterResponseDto registerUser(UserRegisterRequestDto request);

  UserLoginResponseDto login(String email, String rawPassword);

  Page<UserListResponseDto> getUsers(String search, Pageable pageable);
}
