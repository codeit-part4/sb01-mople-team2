package com.sprint.mople.domain.user.service;

import com.sprint.mople.domain.user.dto.UserRegisterRequestDto;
import com.sprint.mople.domain.user.dto.UserRegisterResponseDto;

public interface UserService {

  public UserRegisterResponseDto registerUser(UserRegisterRequestDto request);
}
