package com.sprint.mople.domain.user.service;

import com.sprint.mople.domain.user.dto.UserLoginResponseDto;
import com.sprint.mople.domain.user.dto.UserRegisterRequestDto;
import com.sprint.mople.domain.user.dto.UserRegisterResponseDto;

public interface UserService {

  UserRegisterResponseDto registerUser(UserRegisterRequestDto request);

  UserLoginResponseDto login(String email, String rawPassword);
}
