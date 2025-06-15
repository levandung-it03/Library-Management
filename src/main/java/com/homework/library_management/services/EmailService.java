package com.homework.library_management.services;

import com.homework.library_management.exception.AppException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {
    private final JavaMailSender javaMailSender; //--Injection from configured bean in application.properties
    @Value("${spring.mail.username}")
    private String mailSender;

    @Async
    public void sendSimpleEmail(String to, String subject, String text) throws AppException {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message, true);

            mimeMessageHelper.setFrom(mailSender);
            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(text, true);

            javaMailSender.send(message);
        } catch (Exception e) {
            log.error("[EMAIL_SERVICE] Error: {}", e.getMessage());
            throw new AppException(e.getMessage());
        }
    }
}