package com.sprint.mople.domain.user.service;

import com.sprint.mople.domain.user.dto.UserRegisterRequestDto;
import com.sprint.mople.domain.user.dto.UserRegisterResponseDto;
import com.sprint.mople.domain.user.entity.User;
import com.sprint.mople.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;


  public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }

  public UserRegisterResponseDto registerUser(UserRegisterRequestDto request) {
    if (userRepository.existsByEmail(request.getEmail())) {
      throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
    }

    User user = new User();
    user.setUserName(request.getName());
    user.setEmail(request.getEmail());
    //비밀번호 BCrypt 암호화
    String encoded = passwordEncoder.encode(request.getPassword());
    user.setPassword(encoded);

    User saved = userRepository.save(user);

    return new UserRegisterResponseDto(saved.getId(), saved.getUserName(), saved.getEmail());
  }
}

