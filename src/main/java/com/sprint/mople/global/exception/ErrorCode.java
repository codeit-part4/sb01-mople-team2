package com.sprint.mople.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

  REVIEW_NOT_FOUND(404, "R001", "리뷰를 찾을 수 없습니다."),

  USER_NOT_FOUND(404, "U001", "사용자를 찾을 수 없습니다."),

  DUPLICATE_REVIEW(400, "R002", "이미 등록된 리뷰입니다."),

  FOLLOW_NOT_FOUND(404, "F001", "팔로우 관계를 찾을 수 없습니다.");

  private final int status;   // HTTP Status
  private final String code;     // 도메인 식별 코드
  private final String message;  // 기본 메시지
}
