package com.sprint.mople.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

  CONTENT_NOT_FOUND(404, "C001", "콘텐츠를 찾을 수 없습니다."),
  CONTENT_ALREADY_LIKED(400, "C005", "이미 좋아요한 컨텐츠입니다."),
  CONTENT_NOT_LIKED(400, "C006", "좋아요하지 않은 컨텐츠입니다."),

  WATCH_SESSION_NOT_FOUND(404, "W001", "시청 세션을 찾을 수 없습니다."),
  SESSION_ALREADY_JOINED(400, "W002", "이미 해당 시청 세션에 참여했습니다."),
  PARTICIPANT_NOT_FOUND(404, "W003", "시청 세션 참여자를 찾을 수 없습니다."),

  REVIEW_NOT_FOUND(404, "R001", "리뷰를 찾을 수 없습니다."),

  PLAYLIST_NOT_FOUND(404, "P001", "플레이리스트를 찾을 수 없습니다."),
  PLAYLIST_ILLEGAL_ACCESS(403, "P002", "플레이리스트에 접근할 수 없습니다."),
  DUPLICATE_PLAYLIST_CONTENT(400, "P003", "이미 등록된 플레이리스트 콘텐츠입니다."),
  PLAYLIST_CONTENT_NOT_FOUND(404, "P004", "플레이리스트 콘텐츠를 찾을 수 없습니다."),
  PLAYLIST_ALREADY_LIKED(400, "P005", "이미 좋아요한 플레이리스트입니다."),
  PLAYLIST_NOT_LIKED(400, "P006", "좋아요하지 않은 플레이리스트입니다."),

  USER_NOT_FOUND(404, "U001", "사용자를 찾을 수 없습니다."),

  DUPLICATE_REVIEW(400, "R002", "이미 등록된 리뷰입니다."),

  FOLLOW_NOT_FOUND(404, "F001", "팔로우 관계를 찾을 수 없습니다."),

  FOLLOW_ALREADY_EXISTS(400, "F002", "이미 팔로우 관계가 존재합니다."),

  CHAT_ROOM_NOT_FOUND(404, "C001", "채팅방을 찾을 수 없습니다."),

  NOTIFICATION_NOT_FOUND(404, "N001", "알림을 찾을 수 없습니다."),

  FORBIDDEN_ACCESS(403, "C001", "해당 리소스에 접근할 권한이 없습니다.");

  private final int status;   // HTTP Status
  private final String code;     // 도메인 식별 코드
  private final String message;  // 기본 메시지
}
