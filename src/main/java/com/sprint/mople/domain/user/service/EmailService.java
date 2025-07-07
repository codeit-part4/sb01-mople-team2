package com.sprint.mople.domain.user.service;

public interface EmailService {
  void sendTempPassword(String email, String tempPassword);
}

