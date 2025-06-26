package com.sprint.mople.domain.user.service;

import com.sprint.mople.domain.user.dto.UserListResponseDto;
import com.sprint.mople.domain.user.dto.UserLoginResponseDto;
import com.sprint.mople.domain.user.dto.UserRegisterRequestDto;
import com.sprint.mople.domain.user.dto.UserRegisterResponseDto;
import com.sprint.mople.domain.user.entity.Role;
import com.sprint.mople.domain.user.entity.User;
import com.sprint.mople.domain.user.entity.UserSource;
import com.sprint.mople.domain.user.exception.EmailAlreadyExistsException;
import com.sprint.mople.domain.user.exception.LoginFailedException;
import com.sprint.mople.domain.user.repository.UserRepository;
import com.sprint.mople.global.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtProvider jwtProvider;

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

  @Override
  public UserLoginResponseDto login(String email, String password) {
    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new LoginFailedException("이메일 또는 비밀번호가 일치하지 않습니다."));

    if (!passwordEncoder.matches(password, user.getPassword())) {
      throw new LoginFailedException("이메일 또는 비밀번호가 일치하지 않습니다.");
    }

    String token = jwtProvider.createToken(user.getId().toString(), user.getEmail());

    return UserLoginResponseDto.builder()
        .accessToken(token)
        .tokenType("Bearer")
        .expiresIn(jwtProvider.getExpirationSeconds())
        .userId(user.getId())
        .email(user.getEmail())
        .name(user.getUserName())
        .build();
  }

  @Override
  public Page<UserListResponseDto> getUsers(String search, Pageable pageable) {
    Specification<User> spec = getUserSearchSpec(search);

    return userRepository.findAll(spec, pageable)
        .map(user -> new UserListResponseDto(
            user.getUserName(),
            user.getEmail(),
            user.getIsLocked(),
            user.getCreateAt()
        ));
  }

  private Specification<User> getUserSearchSpec(String search) {
    return (root, query, cb) -> {
      if (search == null || search.trim().isEmpty()) {
        return cb.conjunction();
      }

      String like = "%" + search.toLowerCase() + "%";
      return cb.or(
          cb.like(cb.lower(root.get("userName")), like),
          cb.like(cb.lower(root.get("email")), like)
      );
    };
  }

}

