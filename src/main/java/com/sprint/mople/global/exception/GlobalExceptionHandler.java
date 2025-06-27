package com.sprint.mople.global.exception;

import com.sprint.mople.domain.user.exception.AccountLockedException;
import com.sprint.mople.domain.user.exception.EmailAlreadyExistsException;
import com.sprint.mople.domain.user.exception.LoginFailedException;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class GlobalExceptionHandler {

  //이메일 중복 exception
  @ExceptionHandler(EmailAlreadyExistsException.class)
  public ResponseEntity<Object> handleEmailAlreadyExistsException(EmailAlreadyExistsException ex) {
    Map<String, Object> body = new LinkedHashMap<>();
    body.put("exceptionName", ex.getClass().getSimpleName());
    body.put("message", ex.getMessage());

    List<String> details = List.of(
        "이 이메일은 이미 사용 중입니다.",
        "이메일 주소를 다시 확인해주세요.",
        "중복 이메일 등록은 허용되지 않습니다."
    );

    body.put("details", details);

    return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
  }

  //이메일 형식 exception
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
    List<String> errors = ex.getBindingResult().getFieldErrors().stream()
        .map(error -> error.getField() + ": " + error.getDefaultMessage())
        .toList();

    Map<String, Object> body = new LinkedHashMap<>();
    body.put("exceptionName", ex.getClass().getSimpleName());
    body.put("message", "입력값이 유효하지 않습니다.");
    body.put("details", errors);

    return ResponseEntity.badRequest().body(body);
  }

  @ExceptionHandler(LoginFailedException.class)
  public ResponseEntity<Object> handleInvalidCredentials(LoginFailedException ex) {
    Map<String, Object> body = new LinkedHashMap<>();
    body.put("exceptionName", ex.getClass().getSimpleName());
    body.put("message", ex.getMessage());

    List<String> details = List.of(
        "이메일 또는 비밀번호가 정확하지 않습니다.",
        "입력한 정보를 다시 확인해주세요.",
        "로그인에 실패했습니다."
    );

    body.put("details", details);

    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body);
  }

  @ExceptionHandler(AccountLockedException.class)
  public ResponseEntity<Object> handleAccountLocked(AccountLockedException ex) {
    Map<String, Object> body = new LinkedHashMap<>();
    body.put("exceptionName", ex.getClass().getSimpleName());
    body.put("message", ex.getMessage());

    List<String> details = List.of(
        "이 계정은 현재 잠겨 있습니다.",
        "관리자에게 문의하거나 잠금 해제를 시도해주세요.",
        "보안 정책에 따라 로그인할 수 없습니다."
    );

    body.put("details", details);

    return ResponseEntity.status(HttpStatus.LOCKED).body(body);
  }

  @ExceptionHandler(MopleException.class)
  public ResponseEntity<ErrorResponse> handleMopleException(MopleException ex) {
    ErrorCode ec = ex.getErrorCode();
    ErrorResponse body = new ErrorResponse(ec.getStatus(), ec.getMessage(), ec.getCode(),
        Instant.now());
    return ResponseEntity.status(ec.getStatus()).body(body);
  }
}
