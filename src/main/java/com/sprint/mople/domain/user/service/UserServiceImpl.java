package com.sprint.mople.domain.user.service;

import com.sprint.mople.domain.user.dto.UserRegisterRequestDto;
import com.sprint.mople.domain.user.dto.UserRegisterResponseDto;
import com.sprint.mople.domain.user.entity.Role;
import com.sprint.mople.domain.user.entity.User;
import com.sprint.mople.domain.user.entity.UserSource;
import com.sprint.mople.domain.user.exception.EmailAlreadyExistsException;
import com.sprint.mople.domain.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;


  public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public UserRegisterResponseDto registerUser(UserRegisterRequestDto request) {
    if (userRepository.existsByEmail(request.getEmail())) {
      throw new EmailAlreadyExistsException("이미 존재하는 이메일입니다.");
    }

    User user = new User();
    user.setUserName(request.getName());
    user.setEmail(request.getEmail());

    //비밀번호 BCrypt 암호화
    String encoded = passwordEncoder.encode(request.getPassword());

    //유저 기본값
    user.setPassword(encoded);
    user.setUserSource(UserSource.LOCAL);
    user.setRole(Role.USER);
    user.setIsLocked(Boolean.FALSE);
    user.setIsUsingTempPassword(Boolean.FALSE);

    User saved = userRepository.save(user);

    return new UserRegisterResponseDto(saved.getId(), saved.getUserName(), saved.getEmail());
  }
}

