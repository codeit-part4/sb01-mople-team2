package com.sprint.mople.domain.user.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

  private final JavaMailSender mailSender;

  @Value("${spring.mail.from}")
  private String fromEmail;

  public EmailServiceImpl(JavaMailSender mailSender) {
    this.mailSender = mailSender;
  }

  @Override
  public void sendTempPassword(String toEmail, String tempPassword) {
    MimeMessage message = mailSender.createMimeMessage();

    try {
      MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

      helper.setTo(toEmail);
      helper.setSubject("[Mople] 임시 비밀번호 안내");
      helper.setFrom(fromEmail);

      // HTML 본문
      String content = "<h1>안녕하세요, Mople 입니다.</h1>"
          + "<p>요청하신 임시 비밀번호를 안내드립니다.</p>"
          + "<p><strong>임시 비밀번호: </strong> " + tempPassword + "</p>"
          + "<p>로그인 후 반드시 비밀번호를 변경해 주세요.</p>"
          + "<br>"
          + "<p>감사합니다.</p>";

      helper.setText(content, true);

      mailSender.send(message);
    } catch (MessagingException e) {
      throw new RuntimeException("임시 비밀번호 이메일 전송에 실패했습니다.", e);
    }
  }
}
