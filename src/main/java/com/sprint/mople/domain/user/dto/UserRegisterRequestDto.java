package com.sprint.mople.domain.user.dto;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRegisterRequestDto {
  private String name;
  @Email(message = "올바른 이메일 형식이 아닙니다.")
  private String email;
  private String password;
}
